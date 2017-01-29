package pl.weblang.background.source

import pl.weblang.VerifierServiceSettings
import pl.weblang.background.JobBuilder
import pl.weblang.background.Segment
import pl.weblang.background.VerifierService
import pl.weblang.integration.file.isHit
import pl.weblang.persistence.SegmentVerificationRepository

class SourceSearchDispatcher(val jobBuilder: JobBuilder) {
    fun start(segment: Segment,
              segmentVerificationRepository: SegmentVerificationRepository) {
        val (results, timeStamp) = jobBuilder.createJob(segment).invoke()
        VerifierService.logger.info { results }
        results.forEach { integration, results ->
            results.flatMap { it.indexAndFile.keys }.forEach { position ->
                if (position.isHit) {
                    segmentVerificationRepository.create(SourceSearchResult(VerifierServiceSettings.FRAGMENT_SIZE,
                                                                            position,
                                                                            segment.fileName,
                                                                            integration.name,
                                                                            segment.source.entryNum(),
                                                                            timeStamp))
                }
            }
        }
    }
}


