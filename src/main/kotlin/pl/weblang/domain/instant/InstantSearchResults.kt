package pl.weblang.domain.instant

import pl.weblang.integration.web.WebInstantSearchResult

data class InstantSearchResults(val responses: List<WebInstantSearchResult>) {
    val count: Long by lazy { responses.sumByLong { it.count } }

    /**
     * Used in the HTML template
     */
    @Suppress("UNUSED_PROPERTY")
    val empty get() = count == 0L
}

private inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    val sum: Long = this
            .map { selector(it) }
            .sum()
    return sum
}
