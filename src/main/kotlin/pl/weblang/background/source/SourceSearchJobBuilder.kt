package pl.weblang.background.source

import kotlinx.coroutines.experimental.runBlocking
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.VerifierServiceSettings
import pl.weblang.background.Fragment
import pl.weblang.background.Segment
import pl.weblang.integration.VerifierServiceProvider

class SourceSearchJobBuilder(val tokenizer: LuceneEnglishTokenizer,
                             val verifierProviders: List<VerifierServiceProvider>) {
    fun createJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> SourceSearchJobResult {
        val fragments: List<Fragment> = segment.fragmentize(tokenizer, VerifierServiceSettings.FRAGMENT_SIZE)
        return {
            runBlocking {
                val results = fragments.map { fragment ->
                    val directHitResults = verifierProviders.map { it.findExactHits(fragment) }
                    val suggestions: List<ProviderSuggestions>
                    if (directHitResults.none { it.anyHit }) {
                        suggestions = verifierProviders.map {
                            ProviderSuggestions(it.findWildcardHits(fragment), it)
                        }
                        SegmentResult(directHitResults, suggestions)
                    } else {
                        SegmentResult(directHitResults)
                    }
                }
                SourceSearchJobResult(results.mapIndexed { index, segmentResult ->
                    ProviderResult(segmentResult, verifierProviders[index])
                }, timeStamp)
            }
        }
    }

}

