package pl.weblang.integration.file

import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.CoreAdapter
import pl.weblang.background.Fragment
import pl.weblang.integration.VerifierIntegrationService
import pl.weblang.weblangDir
import java.io.File
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FileService(val fileManager: FileManager) : VerifierIntegrationService {

    override fun verify(fragment: Fragment): FragmentResults {
        val files = fileManager.resourceFiles
        val foundAtPositionInFile = files.map {
            it.tokenizedToWords.containsInOrder(fragment.wordSequence)
        }.zip(files).toMap()
        return FragmentResults(foundAtPositionInFile)
    }
}

class FileManager(luceneEnglishTokenizer: LuceneEnglishTokenizer = LuceneEnglishTokenizer()) {
    val resourceFiles: List<ResourceFile> by FileLoader(luceneEnglishTokenizer)
}

data class ResourceFile(val file: File, val tokenizedToWords: Array<String>) {
    override fun toString(): String {
        return "ResourceFile(file=${file.name})"
    }
}

class FileLoader(val luceneEnglishTokenizer: LuceneEnglishTokenizer) : ReadOnlyProperty<FileManager, List<ResourceFile>> {
    override fun getValue(thisRef: FileManager, property: KProperty<*>): List<ResourceFile> {
        return CoreAdapter.project.projectProperties.weblangDir.listFiles().filter {
            it.isFile.and(it.name.endsWith(".txt"))
        }.map {
            ResourceFile(it, luceneEnglishTokenizer.tokenizeWordsToStrings(it.readText(), ITokenizer.StemmingMode.NONE))
        }
    }

}

fun Array<String>.containsInOrder(wordSequence: List<String>): Int {
    val notFound = -1
    if (size < wordSequence.size) return notFound
    return (0..size - wordSequence.size).firstOrNull { position -> (0..wordSequence.size - 1).all { this[position + it] == wordSequence[it] } }
           ?: notFound
}
