package pl.weblang.gui

import pl.weblang.background.source.ExactHitVO
import pl.weblang.persistence.ExactHitsRepository
import kotlin.reflect.declaredMemberProperties
import kotlin.reflect.memberProperties

class ExactHitsViewModel(exactHitsRepository: ExactHitsRepository) {

    val searchResults = listOf(exactHitsRepository.retrieveAll())
    private val columnNames = ExactHitsRepository.ExactHitsTable.columns.map { it.name }.sortedExcludingId()
    private val indexesAndFields = (1..columnCount).zip(
            ExactHitVO::class.declaredMemberProperties.map { it.name }.sortedExcludingId())

    fun getColumnCount(): Int = searchResults.size - 1

    fun getColumnName(column: Int): String = columnNames[column]
    fun getRowCount(): Int = searchResults.size

    fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val result = searchResults[rowIndex]
        return result.javaClass.kotlin.memberProperties.first { it.name == indexesAndFields[columnIndex].second }.get(
                result) ?: ""
    }

    private fun List<String>.sortedExcludingId(): List<String> {
        return this.sorted().filterNot { it == "id" }
    }

}
