package pl.weblang.integration.web

import org.apache.http.client.utils.URIBuilder
import pl.weblang.instant.InstantSearchProvider
import pl.weblang.instant.view.template.Entry
import pl.weblang.integration.web.google.api.model.Item
import pl.weblang.integration.web.google.api.model.SearchInformation

/**
 * Holds all web integration services
 */
class WebIntegrationController(val instantSearchProviders: List<InstantSearchProvider>) {

    /**
     * Run instant search with all available providers
     */
    fun processInstantSearch(searchedPhrase: List<String>): List<WebInstantSearchResult> {
        val instantSearchResults = instantSearchProviders.map { it.processInstantRequest(searchedPhrase) }
        return instantSearchResults.map { it.toWebInstantSearchResult() }
    }
}

sealed class InstantSearchResponse {
    data class GoogleApiInstantSearchResponse(val searchInformation: SearchInformation,
                                              val items: List<Item>) : InstantSearchResponse()
}

data class WebInstantSearchResult(val count: Long, val name: String, val entries: List<Entry>)

private fun InstantSearchResponse.toWebInstantSearchResult(): WebInstantSearchResult {
    when (this) {
        is InstantSearchResponse.GoogleApiInstantSearchResponse -> return WebInstantSearchResult(
                searchInformation.totalResults,
                "Google API search results",
                items.map(Item::toEntry))
    }
}

private fun Item.toEntry(): Entry = Entry(URIBuilder(link).build(), htmlSnippet)
