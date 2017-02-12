package pl.weblang.background.source

import pl.weblang.integration.NamedProvider

/**
 * Wildcard matches container
 */
class WildcardHit(private val namedProvider: NamedProvider) {

    private val fileWithMatches: MutableMap<String, List<Match<String>>> = mutableMapOf()

    val entries: Iterator<Map.Entry<String, List<Match<String>>>> get() = fileWithMatches.entries.iterator()

    val providerName get() = namedProvider.name

    /**
     * Is there at least one hit in any of the files.
     */
    fun hasAnyHit() = fileWithMatches.isNotEmpty()

    /**
     * Put entry
     */
    fun put(fileName: String, matches: List<Match<String>>) {
        fileWithMatches.put(fileName, matches)
    }

}

