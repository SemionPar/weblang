package pl.weblang.integration.web.google.scraping

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.core.WireMockConfiguration.options
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.Test
import pl.weblang.TestUtils
import pl.weblang.integration.web.UnsuccessfulRestCallException
import pl.weblang.integration.web.google.GoogleRequest
import kotlin.test.assertFailsWith


class GoogleScrapingClientStubTest {

    val client = GoogleScrapingClient()

    @Rule
    @JvmField
    var wireMockRule = WireMockRule(options().dynamicPort())

    @Test
    fun shouldCallGoogleWithDefaultParameters() {
        // given
        val url = "http://localhost:${wireMockRule.port()}/url"
        stubFor(get(urlMatching("/url.*"))
                        .willReturn(aResponse()
                                            .withStatus(200)
                                            .withHeader("Content-Type", "text/html")
                                            .withBody(TestUtils.getResourceAsString("html/google/search_lotsOfEntries.html"))))
        val searchItem = "pets"

        // when
        client.search(GoogleRequest(listOf(searchItem), url = url))

        // then
        verify(getRequestedFor(urlMatching("/url.*"))
                       .withQueryParam("q", matching(".*$searchItem.*"))
                       .withQueryParam("lr", matching("lang_en"))
                       .withQueryParam("cr", matching("countryUS"))
                       .withQueryParam("num", matching("100")))
    }

    @Test
    fun shouldCallGoogleAndHandle503() {
        // given
        val url = "http://localhost:${wireMockRule.port()}/url"
        stubFor(get(urlMatching("/url.*"))
                        .willReturn(aResponse()
                                            .withStatus(503)
                                            .withHeader("Content-Type", "text/html")
                                            .withBody(TestUtils.getResourceAsString("html/google/error_503.html"))))
        val searchItem = "pets"

        // when
        assertFailsWith<UnsuccessfulRestCallException> {
            client.search(GoogleRequest(listOf(searchItem), url = url))
        }

        // then
        verify(getRequestedFor(urlMatching("/url.*"))
                       .withQueryParam("q", matching(".*$searchItem.*"))
                       .withQueryParam("lr", matching("lang_en"))
                       .withQueryParam("cr", matching("countryUS"))
                       .withQueryParam("num", matching("100")))
    }

}
