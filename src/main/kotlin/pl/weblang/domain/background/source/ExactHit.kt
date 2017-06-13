package pl.weblang.domain.background.source

import pl.weblang.domain.background.port.NamedProvider
import pl.weblang.integration.file.isHit

/**
 * Contains map of filenames and indexes of first occurrences of a given fragment within a source file
 */
class ExactHit(private val namedProvider: NamedProvider) {

    private val fileWithIndex: MutableMap<String, Int> = mutableMapOf()

    val hitEntries: Iterator<Map.Entry<String, Int>> get() = fileWithIndex.entries.filter { it.value.isHit }.iterator()

    val providerName get() = namedProvider.name
    /**
     * Is there at least one hit in any of the files
     */
    fun hasAnyHit() = fileWithIndex.any { it.value.isHit }

    /**
     * Put new entry
     */
    fun put(fileName: String, indexOfWords: Int) {
        fileWithIndex.put(fileName, indexOfWords)
    }

}
