package pl.weblang.background

import org.omegat.core.data.SourceTextEntry
import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.BackgroundServiceSettings

/**
 * Abstraction over text segment of translated text
 */
class Segment(val source: SourceTextEntry,
              val translation: String,
              val fileName: String) {

    companion object {
        val empty = Segment(EmptySourceTextEntry.instance, "", "")
        val subgroupTokenizer = SubgroupTokenizer()
    }

    /**
     * Either key is null (happens with empty projects) or translation has not been made yet
     */
    val hasInvalidState: Boolean = this.source.key == null || this.translationIsEqualToSource()

    /**
     * Dissect segment translation to fragments of a given length
     */
    fun fragmentize(tokenizer: LuceneEnglishTokenizer,
                    size: Int = BackgroundServiceSettings.FRAGMENT_SIZE): List<Fragment> {
        return subgroupTokenizer.tokenizeToSubgroups(translation)
                .map { tokenizer.tokenizeWordsToStrings(it, ITokenizer.StemmingMode.NONE) }
                .filter { it.size >= size }
                .map { it.dissectToSubCollections(size) }
                .flatten()
    }

    private fun Array<String>.dissectToSubCollections(fragmentSize: Int): Iterable<Fragment> {
        this.size //todo: assert

        val fragments = ArrayList<Fragment>()

        (0 until (this.size - fragmentSize + 1))
                .mapTo(fragments) { Fragment(this.sliceArray((it)..(fragmentSize + it - 1)).toList()) }

        return fragments
    }

    private fun translationIsEqualToSource() = this.source.srcText == translation

}
