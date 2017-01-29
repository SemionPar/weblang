package pl.weblang.background.forgetful

import pl.weblang.CoreAdapter
import pl.weblang.background.JobBuilder
import pl.weblang.background.Segment

class GlossaryTermSearchDispatcher(val jobBuilder: JobBuilder) {
    fun start(segment: Segment) {
        CoreAdapter.glossaryManager.getGlossaryEntries(segment.source.srcText)
    }

}
