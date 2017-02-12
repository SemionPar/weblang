package pl.weblang.background.source

/**
 * Wildcard matches container
 */
class WildcardHit(val providerName: String) {

    private val fileWithMatches: MutableMap<String, List<Match<String>>> = mutableMapOf()

    val entries: Set<Map.Entry<String, List<Match<String>>>> get() = fileWithMatches.entries


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

