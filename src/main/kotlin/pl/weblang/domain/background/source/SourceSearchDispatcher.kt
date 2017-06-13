package pl.weblang.domain.background.source

import pl.weblang.configuration.BackgroundServiceSettings
import pl.weblang.domain.background.BackgroundService
import pl.weblang.domain.background.Segment
import pl.weblang.persistence.ExactHitsRepository
import pl.weblang.persistence.WildcardHitsRepository

/**
 * Handles dispatching jobs with saving results
 */
class SourceSearchDispatcher(val jobBuilder: SourceSearchJobBuilder,
                             val exactHitsRepository: ExactHitsRepository,
                             val wildcardHitsRepository: WildcardHitsRepository) {
    /**
     * Start new source search for a segment
     */
    fun start(segment: Segment) {
        val (fragmentResults, timeStamp) = runJob(segment)

        val exactHits = fragmentResults.flatMap { it.exactHits }
        persistExactHits(exactHits, segment, timeStamp)

        val wildcardHits = fragmentResults.flatMap { it.wildcardHits }
        persistWildcardHits(segment, timeStamp, wildcardHits)
    }

    private fun runJob(segment: Segment): SourceSearchJobResult {
        val jobResult = jobBuilder.createJob(segment).invoke()
        BackgroundService.logger.info { jobResult }
        return jobResult
    }

    private fun persistWildcardHits(segment: Segment,
                                    timeStamp: Long,
                                    wildcardHits: List<WildcardHit>) {
        for (wildcardHit in wildcardHits) {
            for ((fileName, matches) in wildcardHit.entries) {
                matches.forEach {
                    wildcardHitsRepository.create(WildcardHitVO(BackgroundServiceSettings.FRAGMENT_SIZE,
                                                                it.matchIndexInSource,
                                                                it.wildcardPositionInFragment,
                                                                it.sourceSlice.joinToString(" ", "...", "..."),
                                                                segment.fileName,
                                                                wildcardHit.providerName,
                                                                fileName,
                                                                segment.source.entryNum(),
                                                                timeStamp))
                }
            }
        }
    }

    private fun persistExactHits(exactHits: List<ExactHit>,
                                 segment: Segment,
                                 timeStamp: Long) {
        for (exactHit in exactHits) {
            for ((fileName, index) in exactHit.hitEntries) {
                exactHitsRepository.create(ExactHitVO(BackgroundServiceSettings.FRAGMENT_SIZE,
                                                      index,
                                                      segment.fileName,
                                                      exactHit.providerName,
                                                      fileName,
                                                      segment.source.entryNum(),
                                                      timeStamp))
            }
        }
    }
}

