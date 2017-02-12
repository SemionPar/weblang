package pl.weblang.integration.file

import org.omegat.tokenizer.LuceneEnglishTokenizer

/**
 * Manages files state
 */
class FileManager(luceneEnglishTokenizer: LuceneEnglishTokenizer = LuceneEnglishTokenizer()) {
    val resourceFiles: List<ResourceFile> by FileLoader(
            luceneEnglishTokenizer)
}
