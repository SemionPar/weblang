package pl.weblang.domain.instant

import pl.weblang.integration.web.InstantSearchResponse

interface InstantSearchProvider {
    fun processInstantRequest(searchedPhrase: String): InstantSearchResponse
}
