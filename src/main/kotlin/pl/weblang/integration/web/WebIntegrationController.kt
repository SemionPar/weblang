package pl.weblang.integration.web

import pl.weblang.integration.InstantIntegrationService
import pl.weblang.integration.web.google.api.model.Item
import pl.weblang.integration.web.google.api.model.SearchInformation
import pl.weblang.integration.web.google.scraping.Entry

class WebIntegrationController(val instantIntegrationServices: List<InstantIntegrationService>) {

    fun processInstantSearch(searchedPhrase: List<String>): List<WebInstantSearchResult> {
        val webInstantSearchResults = instantIntegrationServices.map { it.processInstantRequest(searchedPhrase).toWebInstantSearchResult() }
        return webInstantSearchResults
    }

}

sealed class InstantSearchResponse {
    data class ParsedGoogleResponseInstant(val count: Long, val entries: List<Entry>) : InstantSearchResponse() {
        companion object {
            fun emptyInstance() = ParsedGoogleResponseInstant(0, emptyList())
        }
    }

    data class GoogleApiInstantSearchResponse(val searchInformation: SearchInformation,
                                              val items: List<Item>) : InstantSearchResponse()
}

data class WebInstantSearchResult(val count: Long, val name: String, val entries: List<Entry>)

private fun InstantSearchResponse.toWebInstantSearchResult(): WebInstantSearchResult {
    when (this) {
        is InstantSearchResponse.GoogleApiInstantSearchResponse -> return WebInstantSearchResult(searchInformation.totalResults,
                                                                                                 "Google API search results",
                                                                                                 items.map(Item::toEntry))
        is InstantSearchResponse.ParsedGoogleResponseInstant -> return WebInstantSearchResult(count,
                                                                                              "Google search results",
                                                                                              entries)
    }
}

private fun Item.toEntry(): Entry = Entry(link, htmlSnippet)
