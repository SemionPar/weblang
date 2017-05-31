package pl.weblang.instant

import pl.weblang.integration.web.InstantSearchResponse

interface InstantSearchProvider {
    fun processInstantRequest(searchedPhrase: String): InstantSearchResponse
}
