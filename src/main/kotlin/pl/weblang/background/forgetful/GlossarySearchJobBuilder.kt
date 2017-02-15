package pl.weblang.background.forgetful

import pl.weblang.CoreAdapter
import pl.weblang.background.Segment

/**
 * Builds glossary search jobs
 */
class GlossarySearchJobBuilder(private val coreAdapter: CoreAdapter,
                               private val floorMultiplier: Double = 0.9,
                               private val ceilingMultiplier: Double = 1.1) {

    /**
     * Create job for given segment
     */
    fun createJob(segment: Segment,
                  timeStamp: Long = System.currentTimeMillis()): () -> MissingGlossaryEntryJobResult = {
        val glossaryManager = coreAdapter.glossaryManager
        val segmentSource = segment.source.srcText
        val glossaryEntries = glossaryManager.search(segmentSource).filter { segmentSource.contains(it.srcText) }

        if (glossaryEntries.isEmpty()) {
            MissingGlossaryEntryJobResult(false, segment, timeStamp)
        } else {
            val anyEntryMissing = glossaryEntries
                    .map { LevenshteinDistance(segment.translation, it.locText) }
                    .filter { it.fallsIntoRange(floorMultiplier, ceilingMultiplier) }
                    .none()

            MissingGlossaryEntryJobResult(anyEntryMissing, segment, timeStamp)
        }
    }
}

