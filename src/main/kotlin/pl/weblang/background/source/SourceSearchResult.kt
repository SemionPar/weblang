package pl.weblang.background.source

data class SourceSearchResult(
        val fragmentSize: Int,
        val fragmentPosition: Int,
        val file: String,
        val sourceIntegration: String,
        val segmentNumber: Int,
        val timestamp: Long
)
