package pl.weblang.integration.file

import pl.weblang.domain.background.source.WildcardMatch

/**
 * @return True if fragment was found in the collection
 */
val Int.isHit: Boolean
    get() = this != -1

/**
 * Returns a list of single wildcard matches , e.g. (*, x, x)
 */
fun <T> Array<T>.containsWithWildcards(wordSequence: List<T>): List<WildcardMatch<T>> {

    if (this.size < wordSequence.size) return emptyList()
    if (wordSequence.isEmpty() or this.isEmpty()) return emptyList()

    val wildcards = ArrayList<WildcardMatch<T>>()

    for (position in (0..this.size - wordSequence.size)) {
        val sourceSlice = this.sliceArray(position..(position + (wordSequence.size - 1)))
        val sourceAndSearchedPairs = sourceSlice.zip(wordSequence)
        val notEqualPairs = sourceAndSearchedPairs.filter { it.notEqual() }
        val notEqualPairsCount = notEqualPairs.count()

        if (notEqualPairsCount == 1) {
            val wildcardPosition = notEqualPairs.map { sourceAndSearchedPairs.indexOf(it) }.first()
            wildcards.add(WildcardMatch(position, wildcardPosition, sourceSlice))
        }
    }

    return wildcards
}

private fun <A, B> Pair<A, B>.notEqual(): Boolean {
    return this.first != this.second
}

/**
 * @return -1 if not found, occurrence index otherwise
 */
fun <T> Array<T>.containsInOrder(wordSequence: List<T>): Int {

    val notFound = -1

    if ((size < wordSequence.size) or this.isEmpty()) return notFound

    return (0..size - wordSequence.size)
                   .firstOrNull { position ->
                       (0..wordSequence.size - 1)
                               .all { this[position + it] == wordSequence[it] }
                   }
           ?: notFound
}
