package pl.weblang.integration.web

import pl.weblang.integration.web.google.api.model.Item
import pl.weblang.integration.web.google.api.model.SearchInformation
import pl.weblang.integration.web.google.scraping.Entry

class WebIntegrationController(val integrationServices: List<IntegrationService>) {

    fun processInstantSearch(searchedPhrase: List<String>): List<WebInstantSearchResult> {
        val webInstantSearchResults = integrationServices.map { it.processRequest(searchedPhrase).toWebInstantSearchResult() }
        return webInstantSearchResults
    }

}

interface IntegrationService {
    fun processRequest(searchedPhrase: List<String>): SearchResponse

}

sealed class SearchResponse {
    data class ParsedGoogleResponse(val count: Long, val entries: List<Entry>) : SearchResponse() {
        companion object {
            fun emptyInstance() = ParsedGoogleResponse(0, emptyList())
        }
    }

    data class GoogleApiSearchResponse(val searchInformation: SearchInformation,
                                       val items: List<Item>) : SearchResponse()
}

data class WebInstantSearchResult(val count: Long, val name: String, val entries: List<Entry>)

private fun SearchResponse.toWebInstantSearchResult(): WebInstantSearchResult {
    when (this) {
        is SearchResponse.GoogleApiSearchResponse -> return WebInstantSearchResult(searchInformation.totalResults,
                                                                                   "Google API search results",
                                                                                   items.map(Item::toEntry))
        is SearchResponse.ParsedGoogleResponse -> return WebInstantSearchResult(count, "Google search results", entries)
    }
}

private fun Item.toEntry(): Entry = Entry(link, htmlSnippet)
