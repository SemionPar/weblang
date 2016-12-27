package pl.weblang.integration

import pl.weblang.integration.web.IntegrationService
import pl.weblang.integration.web.google.api.GoogleApiClient
import pl.weblang.integration.web.google.api.GoogleApiService

class IntegrationManager(val settings: IntegrationSettings) {
    fun instantSearchIntegrations(): List<IntegrationService> {
        return if (settings.GOOGLE_API_ENABLED) listOf(GoogleApiService(GoogleApiClient())) else emptyList<IntegrationService>()
    }
}

class IntegrationSettings {

    var GOOGLE_API_ENABLED: Boolean = true

}
