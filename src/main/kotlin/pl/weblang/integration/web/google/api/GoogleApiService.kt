package pl.weblang.integration.web.google.api

import pl.weblang.integration.InstantSearchService
import pl.weblang.integration.web.InstantSearchResponse

class GoogleApiService(val client: GoogleApiClient) : InstantSearchService {

    override fun processInstantRequest(searchedPhrase: List<String>): InstantSearchResponse {
        return client.search(GoogleApiRequest(searchedPhrase))
    }
}
