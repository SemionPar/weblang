package pl.weblang.background.forgetful

import kotlinx.coroutines.experimental.runBlocking
import org.apache.commons.lang.StringUtils
import pl.weblang.CoreAdapter
import pl.weblang.background.Segment

class GlossarySearchJobBuilder {

    val topRangeMultiplier = 1.1
    val bottomRangeMultiplier = 0.9

    fun createJob(segment: Segment, timeStamp: Long = System.currentTimeMillis()): () -> MissingGlossaryEntryJobResult {
        return {
            runBlocking {
                val glossaryEntries = CoreAdapter.glossaryManager.search(segment.source.srcText).filter {
                    segment.source.srcText.contains(it.srcText)
                }
                val anyEntryOmittedInTranslation = glossaryEntries
                        .map {
                            LevenshteinDistanceWithGlossaryEntry(
                                    StringUtils.getLevenshteinDistance(segment.translation, it.locText), it)
                        }.onEach(::println)
                        .filter {
                            val lengthOfTheoreticalPE = segment.translation.length - it.glossaryEntry.locText.length
                            println(lengthOfTheoreticalPE)
                            it.levenshteinDistance in ((lengthOfTheoreticalPE * bottomRangeMultiplier)..(lengthOfTheoreticalPE * topRangeMultiplier))
                        }.none()
                MissingGlossaryEntryJobResult(anyEntryOmittedInTranslation, segment, timeStamp)
            }
        }
    }

}

