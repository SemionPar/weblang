package pl.weblang.integration.web.google.api

import pl.weblang.instant.InstantSearchProvider
import pl.weblang.integration.web.InstantSearchResponse

class GoogleApiProvider(val client: GoogleApiClient) : InstantSearchProvider {

    override fun processInstantRequest(searchedPhrase: String): InstantSearchResponse {
        return client.search(GoogleApiRequest(searchedPhrase))
    }
}
