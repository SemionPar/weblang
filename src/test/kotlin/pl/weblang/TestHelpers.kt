package pl.weblang

import org.apache.commons.lang3.StringUtils
import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.integration.file.ResourceFile
import java.io.File


infix fun String?.`should be ignoring whitespace`(other: String) {
    if (this == null) throw AssertionError("Was null, expected $other")
    if (this.removeWhitespaceCharacters() != other.removeWhitespaceCharacters()) throw AssertionError(
            "Was $this, expected $other")
}

private fun String.removeWhitespaceCharacters(): String = StringUtils.deleteWhitespace(this)


class TestUtils {

    companion object {
        val tokenizer = LuceneEnglishTokenizer()
        fun getResourceAsString(resourcePath: String) = javaClass.classLoader.getResourceAsStream(resourcePath).use { it.reader().readText() }
        fun getResource(resourcePath: String) = javaClass.classLoader.getResource(resourcePath).let { File(it.toURI()) }
        fun getResourceAsTokenizedStrings(resourcePath: String): Array<String> = tokenizer.tokenizeWordsToStrings(
                getResourceAsString(resourcePath),
                ITokenizer.StemmingMode.NONE)

        fun getResourceAsResourceFile(resourcePath: String) = ResourceFile(getResource(
                resourcePath),
                                                                           getResourceAsTokenizedStrings(
                                                                                   resourcePath))
    }

}

