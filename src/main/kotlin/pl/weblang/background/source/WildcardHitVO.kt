package pl.weblang.background.source

/**
 * Value object that holds WildcardHit data for persistence layer I/O
 */
data class WildcardHitVO(
        val fragmentSize: Int,
        val fragmentPosition: Int,
        val wildcardPosition: Int,
        val suggestion: String,
        val file: String,
        val sourceIntegration: String,
        val sourceIntegrationFileName: String,
        val segmentNumber: Int,
        val timestamp: Long
)
