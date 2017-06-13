package pl.weblang.domain.background.source

import org.apache.commons.lang.builder.HashCodeBuilder
import java.util.*

/**
 * Represents a wildcard match
 */
data class WildcardMatch<T>(val matchIndexInSource: Int,
                            val wildcardPositionInFragment: Int,
                            val sourceSlice: Array<T>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as WildcardMatch<*>

        if (!Arrays.equals(sourceSlice, other.sourceSlice)
            && matchIndexInSource != other.matchIndexInSource
            && wildcardPositionInFragment != other.wildcardPositionInFragment) return false

        return true
    }

    override fun hashCode(): Int {
        return HashCodeBuilder().append(matchIndexInSource)
                .append(wildcardPositionInFragment)
                .append(sourceSlice)
                .toHashCode()
    }
}
