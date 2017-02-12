package pl.weblang.background.source

data class FragmentResult(val exactHits: List<ExactHit>,
                          val wildcardHits: List<WildcardHit> = emptyList())
