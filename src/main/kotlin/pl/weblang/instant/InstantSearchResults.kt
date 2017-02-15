package pl.weblang.instant

import pl.weblang.integration.web.WebInstantSearchResult

data class InstantSearchResults(val responses: List<WebInstantSearchResult>) {
    val count: Long by lazy { responses.sumByLong { it.count } }

    /**
     * Used in the HTML template
     */
    val empty get() = count == 0L
}
