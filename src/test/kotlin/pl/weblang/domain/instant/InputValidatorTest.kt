package pl.weblang.domain.instant

import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.junit.platform.runner.JUnitPlatform
import org.junit.runner.RunWith
import kotlin.test.assertFailsWith

@RunWith(JUnitPlatform::class)
class InputValidatorTest : Spek({
                                    describe("Input String validator") {
                                        val inputProcessor = InputValidator()
                                        it("Should validate string") {
                                            inputProcessor.validate("a string with spaces")
                                        }
                                        it("Should throw on empty string") {
                                            assertFailsWith<IllegalArgumentException> {
                                                inputProcessor.validate("")
                                            }
                                        }
                                    }
                                })
