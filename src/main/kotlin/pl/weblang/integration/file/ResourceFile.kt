package pl.weblang.integration.file

import java.io.File

data class ResourceFile(val file: File, val tokenizedToWords: Array<String>) {
    override fun toString(): String {
        return "ResourceFile(file=${file.name})"
    }

    fun containsInOrder(wordSequence: List<String>): Int {
        return tokenizedToWords.containsInOrder(wordSequence)
    }

    fun fileName(): String = file.name
}
