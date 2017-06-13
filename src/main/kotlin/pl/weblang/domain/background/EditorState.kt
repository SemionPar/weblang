package pl.weblang.domain.background

import pl.weblang.omegat.CoreAdapter
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * OmegaT editor overlay
 */
class EditorState(onPreviousSegmentChangeHandler: (KProperty<*>, Segment, Segment) -> Unit, coreAdapter: CoreAdapter) {
    var processedFileName: String = coreAdapter.editor.currentFile
    var processedSegment: Segment = Segment.empty
    var previouslyProcessedSegment: Segment by Delegates.observable(Segment.empty, onPreviousSegmentChangeHandler)
}
