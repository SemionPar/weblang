package pl.weblang.background.source

/**
 * Value object to hold source search job results
 */
data class SourceSearchJobResult(val results: List<FragmentResult>,
                                 val timeStamp: Long)
