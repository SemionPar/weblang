package pl.weblang.background

import org.omegat.core.data.SourceTextEntry
import org.omegat.core.events.IEntryEventListener
import pl.weblang.CoreAdapter

/**
 * Listens to changes of processed entries
 */
class ProcessedEntryChangedListener(val editorState: EditorState) : IEntryEventListener {

    override fun onEntryActivated(newEntry: SourceTextEntry?) {
        //todo: test for newEntry -> null
        newEntry?.let {
            val newTranslation = CoreAdapter.editor.currentTranslation
            editorState.previouslyProcessedSegment = editorState.processedSegment
            editorState.processedSegment = Segment(it, newTranslation, editorState.processedFileName)
        }
    }

    override fun onNewFile(activeFileName: String?) {
        activeFileName?.let { editorState.processedFileName = activeFileName }
    }
}
