package pl.weblang.background.source

import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.BackgroundServiceSettings
import pl.weblang.background.Fragment
import pl.weblang.background.Segment
import pl.weblang.integration.VerifierServiceProvider

/**
 * Builds jobs for source search
 */
class SourceSearchJobBuilder(val tokenizer: LuceneEnglishTokenizer,
                             val verifierProviders: List<VerifierServiceProvider>) {
    /**
     * Creates function with job tu run
     */
    fun createJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> SourceSearchJobResult {
        val fragments: List<Fragment> = segment.fragmentize(tokenizer, BackgroundServiceSettings.FRAGMENT_SIZE)
        return {
            val fragmentResults = mutableListOf<FragmentResult>()

            for (fragment in fragments) {
                val exactHits = findExactHits(fragment)
                if (exactHits.isEmpty()) {
                    val wildcardHits = findWildcardHits(fragment)
                    fragmentResults.add(FragmentResult(exactHits, wildcardHits))
                }
            }

            SourceSearchJobResult(fragmentResults, timeStamp)
        }
    }

    private fun findWildcardHits(fragment: Fragment): List<WildcardHit> {
        return verifierProviders
                .map { it.findWildcardHits(fragment) }
                .toList()
    }

    private fun findExactHits(fragment: Fragment): List<ExactHit> {
        return verifierProviders
                .map { it.findExactHits(fragment) }
                .filter { it.hasAnyHit() }
                .toList()
    }

}

