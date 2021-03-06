package pl.weblang.integration.file

import pl.weblang.domain.background.source.WildcardMatch
import java.io.File

/**
 * Resource file abstraction
 */
data class ResourceFile(val file: File, val tokenizedToWords: Array<String>) {
    override fun toString(): String {
        return "ResourceFile(file=${file.name})"
    }

    fun fileName(): String = file.name

    fun containsInOrder(wordSequence: List<String>): Int {
        return tokenizedToWords.containsInOrder(wordSequence)
    }

    fun containsWithWildcards(wordSequence: List<String>): List<WildcardMatch<String>> {
        return tokenizedToWords.containsWithWildcards(wordSequence)
    }
}
