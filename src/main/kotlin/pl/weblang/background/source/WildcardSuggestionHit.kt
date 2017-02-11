package pl.weblang.background.source

data class WildcardSuggestionHit(
        val fragmentSize: Int,
        val fragmentPosition: Int,
        val wildcardPosition: Int,
        val suggestion: String,
        val file: String,
        val sourceIntegration: String,
        val segmentNumber: Int,
        val timestamp: Long
)
