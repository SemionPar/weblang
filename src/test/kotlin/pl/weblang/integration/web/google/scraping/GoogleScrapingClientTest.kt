package pl.weblang.integration.web.google.scraping

import io.kotlintest.specs.FlatSpec
import org.junit.Ignore
import pl.weblang.integration.web.google.GoogleRequest

@Ignore
class GoogleScrapingClientTest : FlatSpec() {

    val client = GoogleScrapingClient()

    init {
        "Google client" should "make GET request to Google" {
            val page = client.search(GoogleRequest(listOf("pets")))
            page.splitToSequence(' ').toList() should contain("Google")
        }
        "Google client" should "handle sequence as exact match" {
            val exactMatch = listOf("man", "in", "the", "box")
            val page = client.search(GoogleRequest(exactMatch))
            page.contains(exactMatch.reduce { s1, s2 -> s1 + " " + s2 }, true) shouldBe true
        }
    }
}
