package pl.weblang.domain.background.gui

import com.google.common.collect.Lists
import pl.weblang.domain.background.forgetful.MissingGlossaryEntry
import pl.weblang.domain.background.source.ExactHitVO
import pl.weblang.persistence.MissingGlossaryEntryRepository
import pl.weblang.persistence.Repository
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.memberProperties

/**
 * Generic abstract ViewModel adapter
 */
abstract class ViewModel<out T>(private val repository: Repository<T>) {

    protected val entries: List<T> get() = Lists.newArrayList(repository.retrieveAll())

    fun getColumnCount(): Int = repository.getFieldNames().size - 1

    fun getColumnName(column: Int): String = repository.getFieldNames().sortedExcludingId()[column]

    fun getRowCount(): Int = entries.size

    abstract fun getValueAt(rowIndex: Int, columnIndex: Int): Any
}

class ExactHitsViewModel(repository: Repository<ExactHitVO>) : ViewModel<ExactHitVO>(repository) {
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val row = entries[rowIndex]
        val kClass = ExactHitVO::class
        val classFieldNames = kClass.declaredMemberProperties
                .map { it.name }
                .sortedExcludingId()
        return kClass.java.kotlin.memberProperties.first { it.name == classFieldNames[columnIndex] }.get(
                row) ?: ""
    }
}

class WildcardHitsViewModel(repository: MissingGlossaryEntryRepository) : ViewModel<MissingGlossaryEntry>(repository) {
    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any {
        val row = entries[rowIndex]
        val kClass = MissingGlossaryEntry::class
        val classFieldNames = kClass.declaredMemberProperties
                .map { it.name }
                .sortedExcludingId()
        return kClass.java.kotlin.memberProperties.first { it.name == classFieldNames[columnIndex] }.get(
                row) ?: ""
    }

}

fun List<String>.sortedExcludingId(): List<String> {
    return this.sorted().filterNot { it == "id" }
}
