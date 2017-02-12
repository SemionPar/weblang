package pl.weblang.background.forgetful

import pl.weblang.background.BackgroundService
import pl.weblang.background.Segment
import pl.weblang.persistence.MissingGlossaryEntryRepository

class GlossarySearchDispatcher(val jobBuilder: GlossarySearchJobBuilder,
                               val missingGlossaryEntryRepository: MissingGlossaryEntryRepository) {
    fun start(segment: Segment) {
        val result = jobBuilder.createJob(segment).invoke()
        BackgroundService.logger.info { result }
        if (result.anyEntryOmittedInTranslation) {
            missingGlossaryEntryRepository.create(
                    MissingGlossaryEntry(result.segment.fileName, result.segment.source.entryNum(), result.timeStamp))
        }
    }
}
