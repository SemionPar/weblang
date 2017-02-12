package pl.weblang.gui

import com.google.common.collect.Lists
import pl.weblang.background.source.ExactHitVO
import pl.weblang.persistence.ExactHitsRepository
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * Adapts ExactHits for table display
 */
class ExactHitsViewModel(private val exactHitsRepository: ExactHitsRepository) {

    private val entries get() = Lists.newArrayList(exactHitsRepository.retrieveAll())

    fun getColumnCount(): Int = exactHitsRepository.getFieldNames().size - 1

    fun getColumnName(column: Int): String = exactHitsRepository.getFieldNames().sortedExcludingId()[column]

    fun getRowCount(): Int = entries.size

    fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val row = entries[rowIndex]
        val classFieldNames = ExactHitVO::class.declaredMemberProperties
                .map { it.name }
                .sortedExcludingId()
        return row.javaClass.kotlin.memberProperties.first { it.name == classFieldNames[columnIndex] }.get(row) ?: ""
    }

    private fun List<String>.sortedExcludingId(): List<String> {
        return this.sorted().filterNot { it == "id" }
    }
}
