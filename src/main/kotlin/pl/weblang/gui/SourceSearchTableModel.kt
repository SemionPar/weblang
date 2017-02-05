package pl.weblang.gui

import pl.weblang.background.source.SourceSearchResult
import pl.weblang.persistence.SegmentVerificationRepository
import javax.swing.table.AbstractTableModel
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.memberProperties

class SourceSearchTableModel(segmentVerificationRepository: SegmentVerificationRepository) : AbstractTableModel() {

    val sourceSearchResults = segmentVerificationRepository.retrieveAll()
    private val columnCount = SegmentVerificationRepository.SourceSearchResults.columns.size
    private val columnNames = SegmentVerificationRepository.SourceSearchResults.columns.onEach(::println).map { it.name }.onEach {
        println("$it/$columnCount")
    }.slice(1..(columnCount - 1))
    private val indexesAndFields = (1..columnCount).zip(SourceSearchResult::class.declaredMemberProperties.map { it.name })

    override fun getColumnCount(): Int = columnCount - 1

    override fun getColumnName(column: Int): String = columnNames[column]

    override fun getRowCount(): Int = sourceSearchResults.size

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val result = sourceSearchResults[rowIndex]
        return result.javaClass.kotlin.memberProperties.first { it.name == indexesAndFields[columnIndex].second }.get(
                result) ?: ""
    }

}
