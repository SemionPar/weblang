package pl.weblang.background

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pl.weblang.background.source.SourceSearchDispatcher
import pl.weblang.background.source.SourceSearchJobBuilder
import pl.weblang.background.source.SourceSearchJobResult
import pl.weblang.persistence.DirectHitsRepository
import pl.weblang.persistence.SuggestionsRepository

class SourceSearchDispatcherTest : Spek({
    describe("Source search dispatcher") {
        val dispatcher = SourceSearchDispatcher(mock<SourceSearchJobBuilder> {
            on { createJob(any(), any()) } doReturn {
                SourceSearchJobResult(emptyList(),
                        1L)
            }
        }, mock<DirectHitsRepository>(), mock<SuggestionsRepository>())
        it("should start job") {
            dispatcher.start(Segment(EmptySourceTextEntry.instance,
                    "translation",
                    "source.txt"))
        }
    }
})

