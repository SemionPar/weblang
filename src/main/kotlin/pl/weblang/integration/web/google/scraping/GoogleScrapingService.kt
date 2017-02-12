package pl.weblang.integration.web.google.scraping

import pl.weblang.integration.web.InstantSearchResponse
import pl.weblang.integration.web.google.GoogleRequest

class GoogleScrapingService(val client: Client, val parser: GoogleParser) {

    fun processRequest(searchedPhrase: List<String>): InstantSearchResponse.ParsedGoogleResponseInstant {
        val html = client.search(GoogleRequest(searchedPhrase,
                                               url = GOOGLE_SCRAPING_URL))
        return parser.parse(html)
    }

}
