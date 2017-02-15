package pl.weblang.background.forgetful

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.given
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.omegat.core.data.EntryKey
import org.omegat.core.data.SourceTextEntry
import org.omegat.gui.glossary.GlossaryEntry
import org.omegat.gui.glossary.GlossaryManager
import pl.weblang.CoreAdapter
import pl.weblang.background.Segment

@RunWith(JUnitPlatform::class)
class GlossarySearchJobBuilderTest : Spek({
    given("a builder") {
        val glossaryEntries = listOf(assembleGlossaryEntry("algorytm sterowania punktem pracy ogniw fotowoltaicznych",
                "PV cell operating point control algorithm"),
                assembleGlossaryEntry("automat wjazdowy", "parking entry station"),
                assembleGlossaryEntry("automatyka zabezpieczeniowa", "automated protection systems"))

        val glossary = mock<GlossaryManager> {
            on { search(ArgumentMatchers.anyString()) } doReturn glossaryEntries
        }

        val coreAdapter = mock<CoreAdapter> {
            on { glossaryManager } doReturn glossary
        }

        val builder = GlossarySearchJobBuilder(coreAdapter)

        on("segment that contains glossary entry and uses it in unchanged version") {
            val sourceText = "Ten algorytm sterowania punktem pracy ogniw fotowoltaicznych jest nieoptymalny."
            val translation = "This PV cell operating point control algorithm is inefficient."
            val segment = assembleSegment(sourceText, translation)

            it("should not trigger missed glossary entry alert") {

                val missingGlossaryEntryJobResult = builder.createJob(segment).invoke()

                missingGlossaryEntryJobResult.anyEntryOmittedInTranslation `should equal` false
            }
        }

        on("segment that contains glossary entry and uses it in slightly changed version") {
            val sourceText = "Ten algorytm sterowania punktem pracy ogniw fotowoltaicznych jest nieoptymalny."
            val translation = "These PV cell operating point control algorithms are inefficient."
            val segment = assembleSegment(sourceText, translation)

            it("should not trigger missed glossary entry alert") {

                val missingGlossaryEntryJobResult = builder.createJob(segment).invoke()

                missingGlossaryEntryJobResult.anyEntryOmittedInTranslation `should equal` false
            }
        }

        on("segment that contains glossary entry, but does not use the suggested translation") {
            val sourceText = "Ten algorytm sterowania punktem pracy ogniw fotowoltaicznych jest nieoptymalny."
            val translation = "This algorithm is not the most efficient out there."
            val segment = assembleSegment(sourceText, translation)

            it("should trigger missed glossary entry alert") {

                val missingGlossaryEntryJobResult = builder.createJob(segment).invoke()

                missingGlossaryEntryJobResult.anyEntryOmittedInTranslation `should equal` true
            }
        }

        on("segment that does not contain any glossary entry") {
            val sourceText = "Ten algorytm jest nieoptymalny."
            val translation = "This algorithm is not efficient."
            val segment = assembleSegment(sourceText, translation)

            it("should not trigger missed glossary entry alert") {

                val missingGlossaryEntryJobResult = builder.createJob(segment).invoke()

                missingGlossaryEntryJobResult.anyEntryOmittedInTranslation `should equal` false
            }
        }
    }
})

private fun assembleSegment(sourceText: String, translation: String): Segment {
    val sourceTextEntry = SourceTextEntry(EntryKey("", sourceText, "", "", "", ""), 0, null, "", emptyList())
    return Segment(sourceTextEntry, translation, "irrelevent fileName")
}

private fun assembleGlossaryEntry(sourceText: String, targetText: String): GlossaryEntry {
    return GlossaryEntry(sourceText, targetText, "", false)
}
