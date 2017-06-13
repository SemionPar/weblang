package pl.weblang.domain.background.port

import pl.weblang.domain.background.Fragment
import pl.weblang.domain.background.source.ExactHit
import pl.weblang.domain.background.source.WildcardHit

interface SearchServiceProvider : NamedProvider {
    fun findExactHits(fragment: Fragment): ExactHit
    fun findWildcardHits(fragment: Fragment): WildcardHit
}
