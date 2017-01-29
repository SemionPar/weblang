package pl.weblang.integration

import pl.weblang.background.Fragment
import pl.weblang.integration.file.FileManager
import pl.weblang.integration.file.FileService
import pl.weblang.integration.file.FragmentResults
import pl.weblang.integration.web.InstantSearchResponse
import pl.weblang.integration.web.google.api.GoogleApiClient
import pl.weblang.integration.web.google.api.GoogleApiService

class IntegrationManager(val settings: IntegrationSettings) {
    fun instantSearchIntegrations(): List<InstantIntegrationService> {
        return if (settings.GOOGLE_API_ENABLED) listOf(GoogleApiService(GoogleApiClient())) else emptyList<InstantIntegrationService>()
    }

    fun verifierIntegrations(): List<VerifierIntegrationService> {
        if (!settings.VERIFIER_ENABLED) return emptyList()
        return listOf(FileService(FileManager(), "File service"))
    }
}

interface VerifierIntegrationService {
    val name: String
    fun verify(fragment: Fragment): FragmentResults
}


interface InstantIntegrationService {
    fun processInstantRequest(searchedPhrase: List<String>): InstantSearchResponse

}

class IntegrationSettings {

    var GOOGLE_API_ENABLED: Boolean = true
    var VERIFIER_ENABLED: Boolean = true

}
