package pl.weblang.gui

import pl.weblang.background.source.SourceDirectHit
import pl.weblang.persistence.DirectHitsRepository
import javax.swing.table.AbstractTableModel
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.memberProperties

class SourceSearchTableModel(directHitsRepository: DirectHitsRepository) : AbstractTableModel() {

    val sourceSearchResults = directHitsRepository.retrieveAll()
    private val columnCount = DirectHitsRepository.SourceSearchResults.columns.size
    private val columnNames = DirectHitsRepository.SourceSearchResults.columns.map { it.name }.sortedExcludingId()
    private val indexesAndFields = (1..columnCount).zip(
            SourceDirectHit::class.declaredMemberProperties.map { it.name }.sortedExcludingId())

    override fun getColumnCount(): Int = columnCount - 1

    override fun getColumnName(column: Int): String = columnNames[column]

    override fun getRowCount(): Int = sourceSearchResults.size

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val result = sourceSearchResults[rowIndex]
        return result.javaClass.kotlin.memberProperties.first { it.name == indexesAndFields[columnIndex].second }.get(
                result) ?: ""
    }

}

private fun List<String>.sortedExcludingId(): List<String> {
    return this.sorted().filterNot { it == "id" }
}
