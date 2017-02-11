package pl.weblang.background.source

import java.util.*

data class Suggestion<T>(val positionInSource: Int,
                         val wildcardPosition: Int,
                         val sourceSlice: Array<T>) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other?.javaClass != javaClass) return false

        other as Suggestion<*>

        if (!Arrays.equals(sourceSlice, other.sourceSlice)
            && positionInSource != other.positionInSource
            && wildcardPosition != other.wildcardPosition) return false

        return true
    }

    override fun hashCode(): Int {
        return Arrays.hashCode(sourceSlice)
    }
}
