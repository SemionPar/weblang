package pl.weblang.domain.background

import org.amshove.kluent.`should be`
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import org.omegat.tokenizer.LuceneEnglishTokenizer

@RunWith(JUnitPlatform::class)
class SegmentTest : Spek({
                             describe("Segment that holds a source entry, translation and filename") {
                                 val tokenizer = LuceneEnglishTokenizer()

                                 context("Segment should fragmentize itself to relevant fragments") {

                                     it("should fragmentize plain sentence") {
                                         val sentence = "Kotlin strings are exactly the same things as Java strings."
                                         val fileName = "fileName"
                                         val segment = Segment(EmptySourceTextEntry.instance,
                                                               sentence,
                                                               fileName)
                                         val size = 3

                                         val fragments = segment.fragmentize(tokenizer, size)

                                         println(fragments.joinToString(separator = "|"))
                                         fragments.forEach { it.wordSequence.size `should be` size }
                                         fragments.size `should be` 8
                                     }

                                     it("should fragmentize sentence with parenthesis") {
                                         val sentence = "He finally answered (after taking five minutes to think) that he did not understand the question."
                                         val fileName = "fileName"
                                         val segment = Segment(EmptySourceTextEntry.instance,
                                                               sentence,
                                                               fileName)
                                         val size = 3

                                         val fragments = segment.fragmentize(tokenizer, size)

                                         println(fragments.joinToString(separator = "|"))
                                         fragments.size `should be` 10
                                     }

                                 }
                             }
                             describe("Sentence should be split into logical subgroups according to separator placement") {
                                 val subgroupTokenizer = SubgroupTokenizer()
                                 it("should divide plain sentence into single subgroup") {
                                     val sentence = "Kotlin strings are exactly the same things as Java strings."

                                     val subgroups = subgroupTokenizer.tokenizeToSubgroups(sentence)

                                     subgroups.size `should be` 1
                                 }
                                 it("should divide sentence into subgroups") {
                                     val sentence = "The maxim goes \"if you cant win them, join them\" and I (to great extent) disagree with it."

                                     val subgroups = subgroupTokenizer.tokenizeToSubgroups(sentence)

                                     println(subgroups.joinToString(separator = "|"))
                                     subgroups.size `should be` 5
                                 }

                             }
                         })
