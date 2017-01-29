package pl.weblang.background

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pl.weblang.background.source.JobResult
import pl.weblang.background.source.SourceSearchDispatcher
import pl.weblang.persistence.SegmentVerificationRepository

class SourceSearchDispatcherTest : Spek({
                                            describe("Source search dispatcher") {
                                                val dispatcher = SourceSearchDispatcher(mock<JobBuilder> {
                                                    on { createJob(any(), any()) } doReturn {
                                                        JobResult(emptyMap(),
                                                                  1L)
                                                    }
                                                })
                                                it("should start job") {
                                                    dispatcher.start(Segment(EmptySourceTextEntry.instance,
                                                                             "translation",
                                                                             "source.txt"),
                                                                     SegmentVerificationRepository())
                                                }
                                            }
                                        })

