package pl.weblang.background.forgetful

data class MissingGlossaryEntry(
        val file: String,
        val segmentNumber: Int,
        val timestamp: Long
)
