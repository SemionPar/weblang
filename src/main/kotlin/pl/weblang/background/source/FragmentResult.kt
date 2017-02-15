package pl.weblang.background.source

/**
 * Value object for source search fragment results; both collections may be empty
 */
data class FragmentResult(val exactHits: List<ExactHit>,
                          val wildcardHits: List<WildcardHit> = emptyList())
