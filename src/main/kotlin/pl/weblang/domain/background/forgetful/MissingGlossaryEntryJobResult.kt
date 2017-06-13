package pl.weblang.domain.background.forgetful

import pl.weblang.domain.background.Segment

/**
 * Value object to hold missing glossary entry job results
 */
data class MissingGlossaryEntryJobResult(val anyEntryOmittedInTranslation: Boolean,
                                         val segment: Segment,
                                         val timeStamp: Long)
