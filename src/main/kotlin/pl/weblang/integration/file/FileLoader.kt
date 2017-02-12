package pl.weblang.integration.file

import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.CoreAdapter
import pl.weblang.weblangDir
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Loads files from weblang project directory
 */
class FileLoader(val luceneEnglishTokenizer: LuceneEnglishTokenizer) : ReadOnlyProperty<FileManager, List<ResourceFile>> {

    override fun getValue(thisRef: FileManager, property: KProperty<*>): List<ResourceFile> {
        return CoreAdapter.project.projectProperties.weblangDir.listFiles().filter {
            it.isFile.and(it.name.endsWith(".txt"))
        }.map {
            ResourceFile(it,
                    luceneEnglishTokenizer.tokenizeWordsToStrings(it.readText(),
                            ITokenizer.StemmingMode.NONE))
        }
    }

}
