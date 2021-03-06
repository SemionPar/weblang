package pl.weblang.domain.background.gui

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import org.apache.http.client.utils.URIBuilder
import org.jetbrains.spek.api.Spek
import org.jetbrains.spek.api.dsl.describe
import org.jetbrains.spek.api.dsl.it
import org.jetbrains.spek.api.dsl.on
import pl.weblang.`should be ignoring whitespace`
import pl.weblang.domain.instant.InstantSearchResults
import pl.weblang.domain.instant.view.template.Entry
import pl.weblang.domain.instant.view.template.Templater
import pl.weblang.integration.web.WebInstantSearchResult

class TemplaterTest : Spek({
                               describe("templater should provide correct html for web instant search results") {

                                   val templater = Templater()

                                   on("full result without html tags") {
                                       val results = mock<InstantSearchResults> {
                                           on { count } doReturn 200L
                                           val responseMocks = listOf(mock<WebInstantSearchResult> {
                                               on { name } doReturn "Google API"
                                               on { count } doReturn 100L
                                               val entryMocks = listOf(mock<Entry> {
                                                   on { uri } doReturn URIBuilder(
                                                           "http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html").build()
                                                   on { text } doReturn "This is a text without html tags."
                                               }, mock<Entry> {
                                                   on { uri } doReturn URIBuilder("http://www.thymeleaf.org").build()
                                                   on { text } doReturn "This is a text without html tags."
                                               })
                                               on { entries } doReturn entryMocks
                                           }, mock<WebInstantSearchResult> {
                                               on { name } doReturn "Faroo API"
                                               on { count } doReturn 100L
                                               val entryMocks = listOf(mock<Entry> {
                                                   on { uri } doReturn URIBuilder(
                                                           "http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html").build()
                                                   on { text } doReturn "This is a text without html tags."
                                               }, mock<Entry> {
                                                   on { uri } doReturn URIBuilder("http://www.thymeleaf.org").build()
                                                   on { text } doReturn "This is a text without html tags."
                                               })
                                               on { entries } doReturn entryMocks
                                           })
                                           on { responses } doReturn responseMocks
                                       }

                                       it("should generate template") {
                                           val generatedHtml = templater.generateHtml(results)
                                           generatedHtml `should be ignoring whitespace` """<html xmlns="http://www.w3.org/1999/xhtml">

                                                                 <body>

                                                                 <div>
                                                                     <p><strong>Total count: 200</strong></p>
                                                                 </div>
                                                                 <div>
                                                                     <table>
                                                                         <tr>
                                                                             <th>Google API</th>
                                                                             <td>Count:100</td>
                                                                         </tr>

                                                                         <tr>
                                                                             <td>
                                                                                 <div>
                                                                                     <p>This is a text without html tags.</p>
                                                                                     <a href="http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html">www.thymeleaf.org</a>
                                                                                 </div>
                                                                             </td>
                                                                         </tr>
                                                                         <tr>
                                                                             <td>
                                                                                 <div>
                                                                                     <p>This is a text without html tags.</p>
                                                                                     <a href="http://www.thymeleaf.org">www.thymeleaf.org</a>
                                                                                 </div>
                                                                             </td>
                                                                         </tr>
                                                                     </table>
                                                                     <table>
                                                                         <tr>
                                                                             <th>Faroo API</th>
                                                                             <td>Count:100</td>
                                                                         </tr>

                                                                         <tr>
                                                                             <td>
                                                                                 <div>
                                                                                     <p>This is a text without html tags.</p>
                                                                                     <a href="http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html">www.thymeleaf.org</a>
                                                                                 </div>
                                                                             </td>
                                                                         </tr>
                                                                         <tr>
                                                                             <td>
                                                                                 <div>
                                                                                     <p>This is a text without html tags.</p>
                                                                                     <a href="http://www.thymeleaf.org">www.thymeleaf.org</a>
                                                                                 </div>
                                                                             </td>
                                                                         </tr>
                                                                     </table>
                                                                 </div>

                                                                 </body>

                                                                 </html>""".trimIndent()
                                       }

                                   }
                                   on("result with html tags") {
                                       val results = mock<InstantSearchResults> {
                                           on { count } doReturn 200L
                                           val responseMocks = listOf(mock<WebInstantSearchResult> {
                                               on { name } doReturn "Faroo API"
                                               on { count } doReturn 100L
                                               val entryMocks = listOf(mock<Entry> {
                                                   on { uri } doReturn URIBuilder(
                                                           "http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html").build()
                                                   on { text } doReturn "This is <strong>strong</strong> text <b>with</b> html <br>tags.&nbsp;"
                                               })
                                               on { entries } doReturn entryMocks
                                           })
                                           on { responses } doReturn responseMocks
                                       }

                                       it("should map object without errors") {
                                           val generatedHtml = templater.generateHtml(results)
                                           generatedHtml `should be ignoring whitespace` """<html xmlns="http://www.w3.org/1999/xhtml">

                                                                 <body>

                                                                 <div>
                                                                     <p><strong>Total count: 200</strong></p>
                                                                 </div>
                                                                 <div>
                                                                     <table>
                                                                         <tr>
                                                                             <th>Faroo API</th>
                                                                             <td>Count:100</td>
                                                                         </tr>

                                                                         <tr>
                                                                             <td>
                                                                                 <div>
                                                                                     <p>This is <strong>strong</strong> text <b>with</b> html <br>tags.&nbsp;</p>
                                                                                     <a href="http://www.thymeleaf.org/doc/tutorials/2.1/usingthymeleaf.html">www.thymeleaf.org</a>
                                                                                 </div>
                                                                             </td>
                                                                         </tr>
                                                                     </table>
                                                                 </div>

                                                                 </body>

                                                                 </html>""".trimIndent()
                                       }

                                   }
                               }
                           })

