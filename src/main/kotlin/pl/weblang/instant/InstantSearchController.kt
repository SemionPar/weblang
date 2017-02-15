package pl.weblang.instant

import pl.weblang.integration.web.WebIntegrationController

/**
 * Main Instant Search feature controller
 */
class InstantSearchController(val webIntegrationController: WebIntegrationController) {

    fun search(selection: String): InstantSearchResults {
        val searchedPhrase = selection.toWords()
        val response = webIntegrationController.processInstantSearch(searchedPhrase)
        return InstantSearchResults(response)
    }

    private fun String.toWords(): List<String> {
        return this.split(" ")
    }
}

inline fun <T> Iterable<T>.sumByLong(selector: (T) -> Long): Long {
    val sum: Long = this
            .map { selector(it) }
            .sum()
    return sum
}
