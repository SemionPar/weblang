package pl.weblang.integration.file

import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import pl.weblang.background.source.Suggestion

@RunWith(JUnitPlatform::class)
class ArrayManipulationTest : Spek({
    describe("Array contains in order another array") {
        context("Array contains another array") {
            val source = arrayOf("this", "is", "test", "too")
            it("should pass when match is in 0 position") {
                val wordSequence = listOf("this", "is", "test")
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
            val source = arrayOf("this", "test", "is", "too")
            it("should not pass") {
                val wordSequence = listOf("this", "is", "test")
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
    describe("Array should contain another array with wildcards") {
        context("Array contains another array with single match") {
            val source = arrayOf("this", "is", "test", "which", "is", "fun", "too")
            it("should match wildcard in 0 position (* x x)") {
                val wordSequence = listOf("*", "which", "is")
                val result = source.containsWithWildcards(wordSequence)
                result `should equal` listOf(Suggestion(2, 0, arrayOf("test", "which", "is")))
            }
            it("should match wildcard in 1 position (x * x)") {
                val wordSequence = listOf("which", "*", "fun")
                val result = source.containsWithWildcards(wordSequence)
                result `should equal` listOf(Suggestion(3, 1, arrayOf("which", "is", "fun")))
            }
            it("should match wildcard in 2 position (x x *)") {
                val wordSequence = listOf("test", "which", "*")
                val result = source.containsWithWildcards(wordSequence)
                result `should equal` listOf(Suggestion(2, 2, arrayOf("test", "which", "is")))
            }
        }
        context("Array contains another array with multiple matches") {
            it("should match wildcard in 0 position (* x x)") {
                val source = arrayOf("this", "is", "test", "which", "is", "fun", "too", "and", "which", "is", "not")
                val wordSequence = listOf("*", "which", "is")
                val result = source.containsWithWildcards(wordSequence)
                result `should equal` listOf<Suggestion<String>>(Suggestion(2, 0, arrayOf("test", "which", "is")),
                        Suggestion(7, 0, arrayOf("and", "which", "is")))
            }
            it("should match wildcard in 1 position (x * x)") {
                val source = arrayOf("0", "1", "2", "which", "blah", "fun", "6", "7", "which", "blah", "fun")
                val wordSequence = listOf("which", "*", "fun")
                val result = source.containsWithWildcards(wordSequence)
                result `should equal` listOf(Suggestion(3, 1, arrayOf("which", "blah", "fun")),
                        Suggestion(8, 1, arrayOf("which", "blah", "fun")))
            }
            it("should match wildcard in 2 position (x x *)") {
                val source = arrayOf("0", "1", "2", "which", "fun", "blah", "6", "7", "which", "fun", "blah")
                val wordSequence = listOf("which", "fun", "*")
                val result = source.containsWithWildcards(wordSequence)
                result `should equal` listOf(Suggestion(3, 2, arrayOf("which", "fun", "blah")),
                        Suggestion(8, 2, arrayOf("which", "fun", "blah")))
            }
        }
    }
})
