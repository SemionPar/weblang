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


class VerifierService(verifierIntegrations: List<VerifierIntegrationService>) {
    companion object : KLogging()

    val verifyPreviouslyProcessedEntry: (KProperty<*>, Segment, Segment) -> Unit = {
        property, old, new ->
        thread(name = "segmentChangeProcessor") {
            if (new.hasInvalidState) return@thread
            async {
                CompletableFuture.supplyAsync { sourceSearchDispatcher.start(new) }
                CompletableFuture.supplyAsync { glossaryTermSearchDispatcher.start(new) }
            }
        }
    }

    val editorState: EditorState by lazy {
        EditorState(verifyPreviouslyProcessedEntry)
    }

    val processedEntryChangedListener = ProcessedEntryChangedListener(editorState)

    fun startBackgroundVerifierService() {
        CoreAdapter.registerEntryEventListener(processedEntryChangedListener)
    }

    private val jobBuilder: JobBuilder = JobBuilder(LuceneEnglishTokenizer(), verifierIntegrations)

    private val sourceSearchDispatcher = SourceSearchDispatcher(jobBuilder)

    private val glossaryTermSearchDispatcher = GlossaryTermSearchDispatcher(jobBuilder)
}

class GlossaryTermSearchDispatcher(val jobBuilder: JobBuilder) {
    fun start(segment: Segment) {
        CoreAdapter.glossaryManager.getGlossaryEntries(segment.source.srcText)
    }

}

class SourceSearchDispatcher(val jobBuilder: JobBuilder) {
    fun start(segment: Segment) {
        val jobResult: JobResult = jobBuilder.createJob(segment).invoke()
        VerifierService.logger.info { jobResult.results }
        SegmentVerification(jobResult, segment)
    }

}

class JobBuilder(val tokenizer: LuceneEnglishTokenizer,
                 val verifierIntegrations: List<VerifierIntegrationService>) {
    fun createJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> JobResult {
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

}

data class Fragment(val wordSequence: List<String>)
data class JobResult(val results: Map<List<FragmentResults>, VerifierIntegrationService>, val timeStamp: Long)
data class SegmentVerification(val jobResult: JobResult, val segment: Segment) {

}

class EditorState(onPreviousSegmentChangeHandler: (KProperty<*>, Segment, Segment) -> Unit) {
    var currentlyProcessedFileName: String = CoreAdapter.editor.currentFile
    var currentlyProcessedSegment: Segment = Segment.empty
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
    val instance = SourceTextEntry(null, 0, null, "", emptyList())
}

class Segment(val source: SourceTextEntry,
              val translation: String,
              val fileName: String) {
    companion object {

        val empty = Segment(EmptySourceTextEntry.instance, "", "")
    }

    val hasInvalidState: Boolean = this.source.key == null || this.translationIsEqualToSource()

    fun fragmentize(tokenizer: LuceneEnglishTokenizer,
                    size: Int = VerifierServiceSettings.FRAGMENT_SIZE): List<Fragment> {
        return translation.createLogicalSubgroups().map {
            tokenizer.tokenizeWordsToStrings(it, ITokenizer.StemmingMode.NONE)
        }
                .filter { it.size >= size }
                .map { it.generateFragment(size) }
                .flatten()

    }

    private fun translationIsEqualToSource() = this.source.srcText.let { it == translation }
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
