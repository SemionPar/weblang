package pl.weblang.background.source

import pl.weblang.integration.NamedProvider
import pl.weblang.integration.file.isHit

/**
 * Contains map of filenames and indexes of first occurrences.
 */
class ExactHit(private val namedProvider: NamedProvider) {

    private val fileWithIndex: MutableMap<String, Int> = mutableMapOf()

    val hitEntries: Iterator<Map.Entry<String, Int>> get() = fileWithIndex.entries.filter { it.value.isHit }.iterator()

    val providerName get() = namedProvider.name
    /**
     * Is there at least one hit in any of the files.
     */
    fun hasAnyHit() = fileWithIndex.any { it.value.isHit }

    /**
     * Put entry
     */
    fun put(fileName: String, indexOfWords: Int) {
        fileWithIndex.put(fileName, indexOfWords)
    }

}
