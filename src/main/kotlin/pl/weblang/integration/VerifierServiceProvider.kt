package pl.weblang.integration

import pl.weblang.background.Fragment
import pl.weblang.background.source.ExactHit
import pl.weblang.background.source.WildcardHit

interface VerifierServiceProvider : NamedProvider {
    fun findExactHits(fragment: Fragment): ExactHit
    fun findWildcardHits(fragment: Fragment): WildcardHit
}
