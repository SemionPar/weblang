package pl.weblang.background.source

import pl.weblang.integration.file.isHit

/**
 * Contains map of filenames and indexes of first occurrences.
 */
class ExactHit(val providerName: String) {

    private val fileWithIndex: MutableMap<String, Int> = mutableMapOf()

    val entries: Set<Map.Entry<String, Int>> get() = fileWithIndex.entries

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
