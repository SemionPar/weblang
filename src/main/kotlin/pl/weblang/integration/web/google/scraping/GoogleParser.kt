package pl.weblang.integration.web.google.scraping

import mu.KLogging
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import pl.weblang.integration.web.InstantSearchResponse

class GoogleParser {
    companion object : KLogging()

    fun parse(html: String): InstantSearchResponse.ParsedGoogleResponseInstant {
        val document = Jsoup.parse(html) ?: return InstantSearchResponse.ParsedGoogleResponseInstant.emptyInstance()
        logger.debug { "Parsed html: $document" }
        val page = GooglePage(document)
        if (page.hasNoResults()) return InstantSearchResponse.ParsedGoogleResponseInstant.emptyInstance()
        else return InstantSearchResponse.ParsedGoogleResponseInstant(page.parseCount(),
                                                                      page.parseEntries())
    }
}

fun Document.isRegularPage(): Boolean {
    return select(".med .card-section").isNotEmpty()
}
