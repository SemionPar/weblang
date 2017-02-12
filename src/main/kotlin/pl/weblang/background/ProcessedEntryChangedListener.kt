package pl.weblang.background

import org.omegat.core.data.SourceTextEntry
import org.omegat.core.events.IEntryEventListener
import pl.weblang.CoreAdapter

/**
 * Listens to changes of processed entries
 */
class ProcessedEntryChangedListener(val editorState: EditorState) : IEntryEventListener {

    override fun onEntryActivated(newEntry: SourceTextEntry?) {
        val newTranslation = CoreAdapter.editor.currentTranslation
        editorState.previouslyProcessedSegment = editorState.currentlyProcessedSegment
        newEntry?.let {
            editorState.currentlyProcessedSegment = Segment(it, newTranslation, editorState.currentlyProcessedFileName)
        }
    }

    override fun onNewFile(activeFileName: String?) {
        activeFileName?.let { editorState.currentlyProcessedFileName = activeFileName }
    }
}
