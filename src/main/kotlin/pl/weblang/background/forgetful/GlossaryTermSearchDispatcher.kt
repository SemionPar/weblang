package pl.weblang.background.forgetful

import pl.weblang.background.Segment
import pl.weblang.background.VerifierService
import pl.weblang.persistence.MissingGlossaryEntryRepository

class GlossaryTermSearchDispatcher(val jobBuilder: GlossaryTermSearchJobBuilder,
                                   val missingGlossaryEntryRepository: MissingGlossaryEntryRepository) {
    fun start(segment: Segment) {
        val result = jobBuilder.createJob(segment).invoke()
        VerifierService.logger.info { result }
        if (result.anyEntryOmittedInTranslation) {
            missingGlossaryEntryRepository.create(
                    MissingGlossaryEntry(result.segment.fileName, result.segment.source.entryNum(), result.timeStamp))
        }
    }
}
