package pl.weblang.integration

import pl.weblang.instant.InstantSearchProvider
import pl.weblang.integration.file.FileManager
import pl.weblang.integration.file.FileServiceProvider
import pl.weblang.integration.web.google.api.GoogleApiClient
import pl.weblang.integration.web.google.api.GoogleApiProvider

class IntegrationManager(val settings: IntegrationSettings) {
    fun instantSearchIntegrations(): List<InstantSearchProvider> {
        return if (settings.GOOGLE_API_ENABLED) listOf(
                GoogleApiProvider(GoogleApiClient())) else emptyList<InstantSearchProvider>()
    }

    fun verifierIntegrations(): List<VerifierServiceProvider> {
        if (!settings.VERIFIER_ENABLED) return emptyList()
        return listOf(FileServiceProvider(FileManager(), "File service"))
    }
}
