package pl.weblang.background.source

import pl.weblang.VerifierServiceSettings
import pl.weblang.background.Segment
import pl.weblang.background.VerifierService
import pl.weblang.persistence.DirectHitsRepository
import pl.weblang.persistence.SuggestionsRepository

class SourceSearchDispatcher(val jobBuilder: SourceSearchJobBuilder,
                             val directHitsRepository: DirectHitsRepository,
                             val suggestionsRepository: SuggestionsRepository) {
    fun start(segment: Segment) {
        val (resultsWithProviders, timeStamp) = jobBuilder.createJob(segment).invoke()
        VerifierService.logger.info { resultsWithProviders }
        for ((segmentResult, verifierServiceProvider) in resultsWithProviders) {
            segmentResult.directHitResults.flatMap { directHitResult
                ->
                directHitResult.indexAndFile.keys
            }.filter(Int::isHit).forEach {
                directHitsRepository.create(SourceDirectHit(VerifierServiceSettings.FRAGMENT_SIZE,
                        it,
                        segment.fileName,
                        verifierServiceProvider.name,
                        segment.source.entryNum(),
                        timeStamp))
            }
            segmentResult.suggestions.forEach { (suggestionsPerSource) ->
                suggestionsPerSource.forEach { (suggestions) ->
                    suggestions.forEach {
                        suggestionsRepository.create(WildcardSuggestionHit(VerifierServiceSettings.FRAGMENT_SIZE,
                                it.positionInSource,
                                it.wildcardPosition,
                                it.sourceSlice.joinToString(" ", "...", "..."),
                                segment.fileName,
                                verifierServiceProvider.name,
                                segment.source.entryNum(),
                                timeStamp))
                    }
                }
            }
        }
    }
}


