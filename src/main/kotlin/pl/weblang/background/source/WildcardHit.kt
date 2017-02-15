package pl.weblang.background.source

import pl.weblang.integration.NamedProvider

/**
 * Wildcard matches container
 */
class WildcardHit(private val namedProvider: NamedProvider) {

    private val fileWithMatches: MutableMap<String, List<WildcardMatch<String>>> = mutableMapOf()

    val entries: Iterator<Map.Entry<String, List<WildcardMatch<String>>>> get() = fileWithMatches.entries.iterator()

    val providerName get() = namedProvider.name

    /**
     * Put entry
     */
    fun put(fileName: String, wildcardMatches: List<WildcardMatch<String>>) {
        fileWithMatches.put(fileName, wildcardMatches)
    }

}

