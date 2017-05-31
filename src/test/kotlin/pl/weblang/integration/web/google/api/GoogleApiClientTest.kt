package pl.weblang.integration.web.google.api

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.core.WireMockConfiguration
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.Rule
import org.junit.Test
import pl.weblang.TestUtils

class GoogleApiClientTest {

    val client = GoogleApiClient()

    @Rule
    @JvmField
    var wireMockRule = WireMockRule(WireMockConfiguration.options().dynamicPort())

    @Test
    fun shouldCallGoogleWithDefaultParameters() {
        // given
        val url = "http://localhost:${wireMockRule.port()}/url"
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/url.*"))
                                 .willReturn(WireMock.aResponse()
                                                     .withStatus(200)
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(TestUtils.getResourceAsString("json/google/api/search_10_results.json"))))
        val searchItem = "pets"

        // when
        client.search(GoogleApiRequest(searchItem, url = url))

        // then
        WireMock.verify(WireMock.getRequestedFor(WireMock.urlMatching("/url.*"))
                                .withQueryParam("q", WireMock.matching(".*$searchItem.*"))
                                .withQueryParam("lr", WireMock.matching("lang_en"))
                                .withQueryParam("cr", WireMock.matching("countryUS"))
                                .withQueryParam("num", WireMock.matching("10"))
                                .withQueryParam("cx", WireMock.matching("015437594077089503277:fiptoh2j4ok"))
                                .withQueryParam("key", WireMock.matching("AIzaSyDKcNTnEYg3bfPc9rAM4I8jM-eP5Si4FCg")))
    }

    @Test
    fun shouldCallGoogleWithDefaultParametersAndHandleNoResults() {
        // given
        val url = "http://localhost:${wireMockRule.port()}/url"
        WireMock.stubFor(WireMock.get(WireMock.urlMatching("/url.*"))
                                 .willReturn(WireMock.aResponse()
                                                     .withStatus(200)
                                                     .withHeader("Content-Type", "application/json")
                                                     .withBody(TestUtils.getResourceAsString("json/google/api/search_noResults.json"))))
        val searchItem = "pets"

        // when
        client.search(GoogleApiRequest(searchItem, url = url))

        // then
        WireMock.verify(WireMock.getRequestedFor(WireMock.urlMatching("/url.*"))
                                .withQueryParam("q", WireMock.matching(".*$searchItem.*"))
                                .withQueryParam("lr", WireMock.matching("lang_en"))
                                .withQueryParam("cr", WireMock.matching("countryUS"))
                                .withQueryParam("num", WireMock.matching("10"))
                                .withQueryParam("cx", WireMock.matching("015437594077089503277:fiptoh2j4ok"))
                                .withQueryParam("key", WireMock.matching("AIzaSyDKcNTnEYg3bfPc9rAM4I8jM-eP5Si4FCg")))
    }
}
