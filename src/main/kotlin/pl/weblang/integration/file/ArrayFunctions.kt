package pl.weblang.integration.file

import pl.weblang.background.source.Suggestion

fun <T> Array<T>.containsWithWildcards(wordSequence: List<T>): List<Suggestion<T>> {
    if (size < wordSequence.size) return emptyList()
    val wildcards = ArrayList<Suggestion<T>>()
    for (position in (0..size - wordSequence.size)) {
        val sourceSlice = this.sliceArray(position..(position + (wordSequence.size - 1)))
        val sourceAndSearched = sourceSlice.zip(wordSequence)
        val notEqualPairsCount = sourceAndSearched.filter { it.notEqual() }.count()
        if (!(notEqualPairsCount > 1 || notEqualPairsCount == 0)) wildcards.add(
                Suggestion(position,
                        sourceAndSearched.filter { it.notEqual() }.map { sourceAndSearched.indexOf(it) }.first(),
                        sourceSlice))
    }
    return wildcards

}

private fun <A, B> Pair<A, B>.notEqual(): Boolean {
    return this.first != this.second
}

fun <T> Array<T>.containsInOrder(wordSequence: List<T>): Int {
    val notFound = -1
    if (size < wordSequence.size) return notFound
    return (0..size - wordSequence.size)
                   .firstOrNull { position ->
                       (0..wordSequence.size - 1)
                               .all { this[position + it] == wordSequence[it] }
                   }
           ?: notFound
}
