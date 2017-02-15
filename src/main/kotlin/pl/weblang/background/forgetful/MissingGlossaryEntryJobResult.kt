package pl.weblang.background.forgetful

import pl.weblang.background.Segment

/**
 * Value object to hold missing glossary entry job results
 */
data class MissingGlossaryEntryJobResult(val anyEntryOmittedInTranslation: Boolean,
                                         val segment: Segment,
                                         val timeStamp: Long)
