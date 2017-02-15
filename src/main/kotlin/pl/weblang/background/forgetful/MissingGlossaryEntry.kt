package pl.weblang.background.forgetful

/**
 * Value object for missing glossary entry data
 */
data class MissingGlossaryEntry(
        val file: String,
        val segmentNumber: Int,
        val timestamp: Long
)
