package pl.weblang.integration.web.google.scraping

import io.kotlintest.specs.FlatSpec
import org.jsoup.Jsoup
import pl.weblang.TestUtils.Companion.getResourceAsString

class GoogleParserTest : FlatSpec() {

    init {
        val parser = GoogleParser()

        "Parser" should "parse html with search item having empty text" {
            val html = getResourceAsString("html/google/search_emptyDescription.html")
            val (count, entries) = parser.parse(html)
            count shouldBe 444000L
            entries.size shouldBe 9
        }
        "Parser" should "parse html with lots of items" {
            val html = getResourceAsString("html/google/search_lotsOfEntries.html")
            val (count, entries) = parser.parse(html)
            count shouldBe 4020000L
            entries.size shouldBe 100
        }
//        "Parser" should "parse html with degraded instant search" {
//            val html = getResourceAsString("html/google/search_degradedPage.html")
//            val (count, entries) = parser.parse(html)
//            count shouldBe 4020000L
//            entries.size shouldBe 10
//        }
        "Parser" should "should ignore suggested results and return empty list when there are no results for the given phrase" {
            val html = getResourceAsString("html/google/search_noResults_suggested.html")
            val (count, entries) = parser.parse(html)
            count shouldBe 0L
            entries.size shouldBe 0
        }
        "Parser" should "should return empty list when there are no results at all for the given phrase" {
            val html = getResourceAsString("html/google/search_noResults_emptySearch.html")
            val (count, entries) = parser.parse(html)
            count shouldBe 0L
            entries.size shouldBe 0
        }
        "String 4,340,000" should "be parsed to number" {
            val firstNumber = "About 4,340,000 results (0.76 seconds)".parseFirstNumber()
            firstNumber shouldBe 4340000L
        }
        "String 4" should "be parsed to number" {
            val firstNumber = "4 results (0.76 seconds)".parseFirstNumber()
            firstNumber shouldBe 4L
        }
    }
}

class GooglePageTest : FlatSpec() {

    init {
        "regular pages should be categorized as such" {
            "page with search item having empty text" {
                val html = getResourceAsString("html/google/search_emptyDescription.html")
                val document = Jsoup.parse(html)
                val googlePage = GooglePage(document)
                googlePage as GooglePage.RegularGooglePage
            }
        }
    }

}
