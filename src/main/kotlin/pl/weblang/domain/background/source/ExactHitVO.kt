package pl.weblang.domain.background.source

/**
 * Value object that holds ExactHit data for persistence layer I/O
 */
data class ExactHitVO(
        val fragmentSize: Int,
        val fragmentPosition: Int,
        val file: String,
        val sourceIntegration: String,
        val sourceIntegrationFileName: String,
        val segmentNumber: Int,
        val timestamp: Long
)
