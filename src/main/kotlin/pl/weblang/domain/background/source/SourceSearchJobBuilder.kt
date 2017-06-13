package pl.weblang.domain.background.source

import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.configuration.BackgroundServiceSettings
import pl.weblang.domain.background.Fragment
import pl.weblang.domain.background.Segment
import pl.weblang.domain.background.port.SearchServiceProvider

/**
 * Builds jobs for source search
 */
class SourceSearchJobBuilder(val tokenizer: LuceneEnglishTokenizer,
                             val searchProviders: Iterable<SearchServiceProvider>) {
    /**
     * Creates function with job tu run
     */
    fun createJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> SourceSearchJobResult {
        val fragments: Iterable<Fragment> = segment.fragmentize(tokenizer, BackgroundServiceSettings.FRAGMENT_SIZE)
        return {
            val fragmentResults = mutableListOf<FragmentResult>()

            for (fragment in fragments) {
                val exactHits = findExactHits(fragment)
                if (exactHits.isEmpty()) {
                    fragmentResults.add(FragmentResult(exactHits, findWildcardHits(fragment)))
                } else {
                    fragmentResults.add(FragmentResult(exactHits))
                }
            }

            SourceSearchJobResult(fragmentResults, timeStamp)
        }
    }

    private fun findWildcardHits(fragment: Fragment): List<WildcardHit> {
        return searchProviders
                .map { it.findWildcardHits(fragment) }
                .toList()
    }

    private fun findExactHits(fragment: Fragment): List<ExactHit> {
        return searchProviders
                .map { it.findExactHits(fragment) }
                .filter { it.hasAnyHit() }
                .toList()
    }

}

