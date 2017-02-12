package pl.weblang.background

import pl.weblang.CoreAdapter
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class EditorState(onPreviousSegmentChangeHandler: (KProperty<*>, Segment, Segment) -> Unit) {
    //todo: CoreAdapter to object
    var currentlyProcessedFileName: String = CoreAdapter.editor.currentFile
    var currentlyProcessedSegment: Segment = Segment.empty
    var previouslyProcessedSegment: Segment by Delegates.observable(Segment.empty, onPreviousSegmentChangeHandler)
}
