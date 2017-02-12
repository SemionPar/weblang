package pl.weblang.background.source

data class ExactHitVO(
        val fragmentSize: Int,
        val fragmentPosition: Int,
        val file: String,
        val sourceIntegration: String,
        val sourceIntegrationFileName: String,
        val segmentNumber: Int,
        val timestamp: Long
)
