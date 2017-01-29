package pl.weblang.background.source

import kotlinx.coroutines.async
import kotlinx.coroutines.await
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.VerifierServiceSettings
import pl.weblang.background.Fragment
import pl.weblang.background.JobBuilder
import pl.weblang.background.Segment
import pl.weblang.integration.VerifierIntegrationService
import java.util.concurrent.CompletableFuture

class SourceSearchJobBuilder(val tokenizer: LuceneEnglishTokenizer,
                             val verifierIntegrations: List<VerifierIntegrationService>) : JobBuilder {
    override fun createJob(segment: Segment, timeStamp: Long): () -> JobResult {
        val fragments: List<Fragment> = segment.fragmentize(tokenizer, VerifierServiceSettings.FRAGMENT_SIZE)
        return {
            async {
                val results = fragments.map { fragment ->
                    CompletableFuture.supplyAsync {
                        verifierIntegrations.map {
                            it.verify(fragment)
                        }
                    }
                }
                JobResult(verifierIntegrations.zip(results.map { it.await() }).toMap(), timeStamp)
            }.get()
        }
    }

}
