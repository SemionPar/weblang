package pl.weblang.domain.background.forgetful

import pl.weblang.domain.background.BackgroundService
import pl.weblang.domain.background.Segment
import pl.weblang.persistence.MissingGlossaryEntryRepository

/**
 * Runs missed glossary item search on segment
 */
class GlossarySearchDispatcher(val jobBuilder: GlossarySearchJobBuilder,
                               val missingGlossaryEntryRepository: MissingGlossaryEntryRepository) {
    /**
     * Start search for segment
     */
    fun start(segment: Segment) {
        val result = jobBuilder.createJob(segment).invoke()
        BackgroundService.logger.info { result }
        if (result.anyEntryOmittedInTranslation) {
            missingGlossaryEntryRepository.create(
                    MissingGlossaryEntry(result.segment.fileName, result.segment.source.entryNum(), result.timeStamp))
        }
    }
}
