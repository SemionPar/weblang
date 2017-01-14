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
                                 describe("Array searching tests") {
                                     context("Array contains another array") {
                                         val source = arrayOf("This", "is", "test", "too")
                                         it("should pass when match is in 0 position") {
                                             val wordSequence = listOf("This", "is", "test")
                                             val result = source.containsInOrder(wordSequence)
                                             result `should be` 0
                                         }
                                         it("should pass when match is in the last position") {
                                             val wordSequence = listOf("is", "test", "too")
                                             val result = source.containsInOrder(wordSequence)
                                             result `should be` 1
                                         }
                                     }
                                     context("Array contains another array not in the order") {
                                         val source = arrayOf("This", "test", "is", "too")
                                         it("should not pass") {
                                             val wordSequence = listOf("This", "is", "test")
                                             val result = source.containsInOrder(wordSequence)
                                             result `should be` -1
                                         }
                                         it("should not pass ") {
                                             val wordSequence = listOf("is", "test", "too")
                                             val result = source.containsInOrder(wordSequence)
                                             result `should be` -1
                                         }
                                     }
                                 }
                                 describe("File managing") {
                                     context("loading test files") {
                                         it("should load") {
                                             val fileManager = mock<FileManager> {
                                                 on { resourceFiles } doReturn listOf(getResourceAsResourceFile("txt/source.txt"),
                                                                                      getResourceAsResourceFile("txt/source2.txt"))
                                             }
                                             val fileService = FileService(fileManager)

                                             val results = fileService.verify(Fragment(listOf("After", "that", "they")))

                                             results.anyHit `should be` true
                                         }
                                     }
                                 }

                             })

