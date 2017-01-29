package pl.weblang.background

import pl.weblang.CoreAdapter
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class EditorState(onPreviousSegmentChangeHandler: (KProperty<*>, Segment, Segment) -> Unit) {
    var currentlyProcessedFileName: String = CoreAdapter.editor.currentFile
    var currentlyProcessedSegment: Segment = Segment.empty
    var previouslyProcessedSegment: Segment by Delegates.observable(Segment.empty, onPreviousSegmentChangeHandler)
}
