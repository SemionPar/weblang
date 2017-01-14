package pl.weblang.background

import kotlinx.coroutines.async
import kotlinx.coroutines.await
import mu.KLogging
import org.omegat.core.data.SourceTextEntry
import org.omegat.core.events.IEntryEventListener
import org.omegat.tokenizer.ITokenizer
import org.omegat.tokenizer.LuceneEnglishTokenizer
import pl.weblang.CoreAdapter
import pl.weblang.VerifierServiceSettings
import pl.weblang.integration.VerifierIntegrationService
import pl.weblang.integration.file.FragmentResults
import java.util.concurrent.CompletableFuture
import kotlin.concurrent.thread
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


class VerifierService(val verifierIntegrations: List<VerifierIntegrationService>) {
    companion object : KLogging()

    val tokenizer = LuceneEnglishTokenizer()

    val verifyPreviouslyProcessedEntry: (KProperty<*>, Segment, Segment) -> Unit = {
        property, old, new ->
        // don't block UI thread
        thread(name = "segmentChangeProcessor") {
            // don't trigger if user didn't change anything
            if (new.source.srcText == new.translation) return@thread
            val jobResult: JobResult = prepareJob(new)()
            CoreAdapter.mainWindow.showMessageDialog("Found hits: ${jobResult.results}")
            logger.info { jobResult.results }
            // save results to database

        }
    }

    private fun prepareJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> JobResult {
        val fragments: List<Fragment> = segment.fragmentize(tokenizer, VerifierServiceSettings.FRAGMENT_SIZE)
        return {
            async {
                val results = fragments.map { fragment ->
                    CompletableFuture.supplyAsync {
                        verifierIntegrations.map {
                            it.verify(fragment)
                        }
                    }.await()
                }.zip(verifierIntegrations).toMap()
                JobResult(results, timeStamp)
            }.get()
        }
    }


    val editorState: EditorState by lazy {
        EditorState(verifyPreviouslyProcessedEntry,
                    { kProperty: KProperty<*>, segment: Segment, segment1: Segment -> })
    }
    val processedEntryChangedListener = ProcessedEntryChangedListener(editorState)

    fun startBackgroundVerifierService() {
        CoreAdapter.registerEntryEventListener(processedEntryChangedListener)
    }
}

class JobResult(val results: Map<List<FragmentResults>, VerifierIntegrationService>, timeStamp: Long) {

}

data class Fragment(val wordSequence: List<String>) {

}

class EditorState(onPreviousSegmentChangeHandler: (KProperty<*>, Segment, Segment) -> Unit,
                  onCurrentSegmentChangeHandler: (KProperty<*>, Segment, Segment) -> Unit) {
    var currentlyProcessedFileName: String = CoreAdapter.editor.currentFile
    var currentlyProcessedSegment: Segment by Delegates.observable(Segment.empty, onCurrentSegmentChangeHandler)
    var previouslyProcessedSegment: Segment by Delegates.observable(Segment.empty, onPreviousSegmentChangeHandler)
}

class ProcessedEntryChangedListener(val editorState: EditorState) : IEntryEventListener {

    override fun onEntryActivated(newEntry: SourceTextEntry?) {
        val newTranslation = CoreAdapter.editor.currentTranslation
        editorState.previouslyProcessedSegment = editorState.currentlyProcessedSegment
        newEntry?.let {
            editorState.currentlyProcessedSegment = Segment(it,
                                                            newTranslation,
                                                            editorState.currentlyProcessedFileName)
        }
    }

    override fun onNewFile(activeFileName: String?) {
        activeFileName?.let { editorState.currentlyProcessedFileName = activeFileName }
    }
}

object EmptySourceTextEntry {
    val instance = SourceTextEntry(null, 0, null, null, emptyList())
}

class Segment(val source: SourceTextEntry,
              val translation: String,
              val fileName: String) {
    companion object {
        val empty = Segment(EmptySourceTextEntry.instance, "", "")
    }


    fun fragmentize(tokenizer: LuceneEnglishTokenizer,
                    size: Int = VerifierServiceSettings.FRAGMENT_SIZE): List<Fragment> {
        return translation.createLogicalSubgroups().map {
            tokenizer.tokenizeWordsToStrings(it, ITokenizer.StemmingMode.NONE)
        }
                .filter { it.size >= size }
                .map { it.generateFragment(size) }
                .flatten()

    }

}

private fun Array<String>.generateFragment(fragmentSize: Int): List<Fragment> {
    val fragments = ArrayList<Fragment>()
    (0 until (this.size - fragmentSize + 1)).mapTo(fragments) {
        Fragment(this.sliceArray((it)..(fragmentSize + it - 1)).toList())
    }
    return fragments
}

fun String.createLogicalSubgroups(vararg delimiters: String = arrayOf("\"",
                                                                      "\'",
                                                                      ".",
                                                                      "(",
                                                                      ")",
                                                                      ";",
                                                                      ":")): List<String> {
    return split(*delimiters).filterNot(String::isEmpty)
}
