package pl.weblang.background.source

import pl.weblang.integration.file.ResourceFile

data class DirectHitResults(val indexAndFile: Map<Int, ResourceFile>) {
    val anyHit = indexAndFile.any { it.key.isHit }
}

data class SuggestionsPerSource(val suggestions: List<Suggestion<String>>, val file: ResourceFile)

val Int.isHit: Boolean
    get() = this != -1


