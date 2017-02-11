package pl.weblang.integration.file

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.amshove.kluent.`should be`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import pl.weblang.TestUtils.Companion.getResourceAsResourceFile
import pl.weblang.background.Fragment

@RunWith(JUnitPlatform::class)
class FileServiceTest : Spek({
    describe("File managing") {
        context("loading test files") {
            it("should load") {
                val fileManager = mock<FileManager> {
                    on { resourceFiles } doReturn listOf(getResourceAsResourceFile("txt/source.txt"),
                            getResourceAsResourceFile("txt/source2.txt"))
                }
                val fileService = FileServiceProvider(fileManager, "File integration")

                val results = fileService.findExactHits(Fragment(listOf("After", "that", "they")))

                results.anyHit `should be` true
            }
        }
    }
})

