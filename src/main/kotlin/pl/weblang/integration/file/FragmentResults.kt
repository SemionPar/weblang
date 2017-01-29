package pl.weblang.integration.file

data class FragmentResults(val indexAndFile: Map<Int, ResourceFile>) {
    val anyHit = indexAndFile.any { it.key.isHit }
}

val Int.isHit: Boolean
    get() = this != -1


