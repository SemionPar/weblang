package pl.weblang.background.forgetful

import pl.weblang.background.Segment

data class MissingGlossaryEntryJobResult(val anyEntryOmittedInTranslation: Boolean,
                                         val segment: Segment, val timeStamp: Long)
