package pl.weblang.gui

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.context
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import pl.weblang.instant.InstantSearchResults
import pl.weblang.integration.web.WebInstantSearchResult
import pl.weblang.integration.web.google.Entry

class TemplaterTest : Spek({
                               describe("templater should provide correct html for web instant search results") {

                                   val templater = Templater()

                                   context("Regular result without html tags") {
                                       val results = mock<InstantSearchResults> {
                                           on { count } doReturn 200L
                                           val responseMocks = listOf(mock<WebInstantSearchResult> {
                                               on { name } doReturn "Google API"
                                               on { count } doReturn 100L
                                               val entryMocks = listOf(mock<Entry> {
                                                   on { link } doReturn "http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html"
                                                   on { text } doReturn "This is a text without html tags."
                                               }, mock<Entry> {
                                                   on { link } doReturn "http://www.thymeleaf.org"
                                                   on { text } doReturn "This is a text without html tags."
                                               })
                                               on { entries } doReturn entryMocks
                                           }, mock<WebInstantSearchResult> {
                                               on { name } doReturn "Faroo API"
                                               on { count } doReturn 100L
                                               val entryMocks = listOf(mock<Entry> {
                                                   on { link } doReturn "http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html"
                                                   on { text } doReturn "This is a text without html tags."
                                               }, mock<Entry> {
                                                   on { link } doReturn "http://www.thymeleaf.org"
                                                   on { text } doReturn "This is a text without html tags."
                                               })
                                               on { entries } doReturn entryMocks
                                           })
                                           on { responses } doReturn responseMocks
                                       }

                                       it("should map object without errors") {
                                           print(templater.generateHtml(results))
                                       }

                                   }
                               }
                           })

