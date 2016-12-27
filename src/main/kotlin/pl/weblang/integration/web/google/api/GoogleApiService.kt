package pl.weblang.integration.web.google.api

import pl.weblang.integration.web.IntegrationService
import pl.weblang.integration.web.SearchResponse

class GoogleApiService(val client: GoogleApiClient) : IntegrationService {

    override fun processRequest(searchedPhrase: List<String>): SearchResponse {
        return client.search(GoogleApiRequest(searchedPhrase))
    }
}
