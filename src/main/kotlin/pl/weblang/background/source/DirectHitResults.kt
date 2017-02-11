package pl.weblang.background.source

import pl.weblang.integration.file.ResourceFile

class DirectHitResults {
    val indexAndFile: Map<Int, ResourceFile>
    val anyHit = indexAndFile.any { it.key.isHit }
    fun put(fileName: String, indexOfWords: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}

val Int.isHit: Boolean
    get() = this != -1


