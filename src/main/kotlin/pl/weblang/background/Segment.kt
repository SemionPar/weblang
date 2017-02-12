package pl.weblang.background

import org.omegat.core.data.SourceTextEntry
import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.BackgroundServiceSettings

class Segment(val source: SourceTextEntry,
              val translation: String,
              val fileName: String) {
    companion object {
        val empty = Segment(EmptySourceTextEntry.instance, "", "")
        val subgroupTokenizer = SubgroupTokenizer()
    }

    val hasInvalidState: Boolean = this.source.key == null || this.translationIsEqualToSource()

    fun fragmentize(tokenizer: LuceneEnglishTokenizer,
                    size: Int = BackgroundServiceSettings.FRAGMENT_SIZE): List<Fragment> {
        return subgroupTokenizer.tokenizeToSubgroups(translation).map {
            tokenizer.tokenizeWordsToStrings(it, ITokenizer.StemmingMode.NONE)
        }.filter { it.size >= size }.map { it.generateFragment(size) }.flatten()
    }

    private fun translationIsEqualToSource() = this.source.srcText == translation

}

private fun Array<String>.generateFragment(fragmentSize: Int): List<Fragment> {
    val fragments = ArrayList<Fragment>()
    (0 until (this.size - fragmentSize + 1)).mapTo(fragments) {
        Fragment(this.sliceArray((it)..(fragmentSize + it - 1)).toList())
    }
    return fragments
}
