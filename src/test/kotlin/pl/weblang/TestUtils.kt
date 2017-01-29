package pl.weblang

import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.integration.file.ResourceFile
import java.io.File

class TestUtils {

    companion object {
        val tokenizer = LuceneEnglishTokenizer()
        fun getResourceAsString(resourcePath: String) = javaClass.classLoader.getResourceAsStream(resourcePath).use { it.reader().readText() }
        fun getResource(resourcePath: String) = javaClass.classLoader.getResource(resourcePath).let { File(it.toURI()) }
        fun getResourceAsTokenizedStrings(resourcePath: String): Array<String> = tokenizer.tokenizeWordsToStrings(
                getResourceAsString(resourcePath),
                ITokenizer.StemmingMode.NONE)
        fun getResourceAsResourceFile(resourcePath: String) = ResourceFile(getResource(resourcePath),
                                                                           getResourceAsTokenizedStrings(resourcePath))
    }

}

