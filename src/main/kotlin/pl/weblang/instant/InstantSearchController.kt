package pl.weblang.instant

import pl.weblang.integration.web.WebInstantSearchResult
import pl.weblang.integration.web.WebIntegrationController

class InstantSearchController(val webIntegrationController: WebIntegrationController) {

    fun search(selection: String): InstantSearchResults {
        val response = webIntegrationController.processInstantSearch(selection.toWords())
        return InstantSearchResults(response)
    }
}

data class InstantSearchResults(val responses: List<WebInstantSearchResult>) {
    val count: Long by lazy { responses.sumByLong { it.count } }
    val empty get() = count == 0L
}

private fun String.toWords(): List<String> {
    return this.split(" ")
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    val sum: Long = this
            .map { selector(it) }
            .sum()
    return sum
}
