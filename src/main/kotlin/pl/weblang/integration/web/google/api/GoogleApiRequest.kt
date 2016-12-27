package pl.weblang.integration.web.google.api

import pl.weblang.GoogleSettings
import pl.weblang.integration.web.google.GoogleRequest
import pl.weblang.integration.web.google.scraping.GOOGLE_API_URL
import pl.weblang.integration.web.google.toExactMatch

class GoogleApiRequest(searchedPhrase: List<String>,
                       language: String = "lang_en",
                       region: String = "countryUS",
                       pageSize: Int = 10,
                       val customSearchEngineId: String = GoogleSettings.CUSTOM_SEARCH_ENGINE_ID,
                       val apiKey: String = GoogleSettings.API_KEY,
                       override val url: String = GOOGLE_API_URL) : GoogleRequest(searchedPhrase,
                                                                                  language,
                                                                                  region,
                                                                                  pageSize,
                                                                                  url) {
    override val queryOptions: Map<String, Any>
        get() = mapOf(Pair("q", toExactMatch(searchedPhrase)),
                      Pair("lr", language),
                      Pair("cr", region),
                      Pair("num", pageSize),
                      Pair("cx", customSearchEngineId),
                      Pair("key", apiKey))
}