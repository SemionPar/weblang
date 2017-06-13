package pl.weblang.domain.background.source

import pl.weblang.domain.background.port.NamedProvider

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

