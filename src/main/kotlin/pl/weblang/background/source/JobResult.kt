package pl.weblang.background.source

import pl.weblang.integration.VerifierIntegrationService
import pl.weblang.integration.file.FragmentResults

data class JobResult(val results: Map<VerifierIntegrationService, List<FragmentResults>>, val timeStamp: Long)
