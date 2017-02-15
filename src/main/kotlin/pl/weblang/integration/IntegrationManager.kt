package pl.weblang.integration

import pl.weblang.background.Fragment
import pl.weblang.background.source.ExactHit
import pl.weblang.background.source.WildcardHit
import pl.weblang.integration.file.FileManager
import pl.weblang.integration.file.FileServiceProvider
import pl.weblang.integration.web.InstantSearchResponse
import pl.weblang.integration.web.google.api.GoogleApiClient
import pl.weblang.integration.web.google.api.GoogleApiService

class IntegrationManager(val settings: IntegrationSettings) {
    fun instantSearchIntegrations(): List<InstantSearchService> {
        return if (settings.GOOGLE_API_ENABLED) listOf(
                GoogleApiService(GoogleApiClient())) else emptyList<InstantSearchService>()
    }

    fun verifierIntegrations(): List<VerifierServiceProvider> {
        if (!settings.VERIFIER_ENABLED) return emptyList()
        return listOf(FileServiceProvider(FileManager(), "File service"))
    }
}

interface VerifierServiceProvider : NamedProvider {
    fun findExactHits(fragment: Fragment): ExactHit
    fun findWildcardHits(fragment: Fragment): WildcardHit
}

interface NamedProvider {
    val name: String
}

interface InstantSearchService {
    fun processInstantRequest(searchedPhrase: List<String>): InstantSearchResponse
}

class IntegrationSettings {

    var GOOGLE_API_ENABLED: Boolean = true
    var VERIFIER_ENABLED: Boolean = true

}

