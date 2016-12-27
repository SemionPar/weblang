package pl.weblang.integration.web.google

import pl.weblang.integration.web.HttpRequest
import pl.weblang.integration.web.google.scraping.GOOGLE_SCRAPING_URL

open class GoogleRequest(val searchedPhrase: List<String>,
                         val language: String = "lang_en",
                         val region: String = "countryUS",
                         val pageSize: Int = 100,
                         override val url: String = GOOGLE_SCRAPING_URL) : HttpRequest {
    override val queryOptions: Map<String, Any>
        get() = mapOf(Pair("q", toExactMatch(searchedPhrase)),
                      Pair("lr", language),
                      Pair("cr", region),
                      Pair("num", pageSize))
}
