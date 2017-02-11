package pl.weblang.integration

import pl.weblang.background.Fragment
import pl.weblang.background.source.DirectHitResults
import pl.weblang.background.source.SuggestionsPerSource
import pl.weblang.integration.file.FileManager
import pl.weblang.integration.file.FileServiceProvider
import pl.weblang.integration.web.InstantSearchResponse
import pl.weblang.integration.web.google.api.GoogleApiClient
import pl.weblang.integration.web.google.api.GoogleApiService

class IntegrationManager(val settings: IntegrationSettings) {
    fun instantSearchIntegrations(): List<InstantIntegrationService> {
        return if (settings.GOOGLE_API_ENABLED) listOf(GoogleApiService(GoogleApiClient())) else emptyList<InstantIntegrationService>()
    }

    fun verifierIntegrations(): List<VerifierServiceProvider> {
        if (!settings.VERIFIER_ENABLED) return emptyList()
        return listOf(FileServiceProvider(FileManager(), "File service"))
    }
}

interface VerifierServiceProvider {
    val name: String
    fun findExactHits(fragment: Fragment): DirectHitResults
    fun findWildcardHits(fragment: Fragment): List<SuggestionsPerSource>
}


interface InstantIntegrationService {
    fun processInstantRequest(searchedPhrase: List<String>): InstantSearchResponse

}

class IntegrationSettings {

    var GOOGLE_API_ENABLED: Boolean = true
    var VERIFIER_ENABLED: Boolean = true

}
