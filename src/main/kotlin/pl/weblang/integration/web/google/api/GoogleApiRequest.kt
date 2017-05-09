package pl.weblang.integration.web.google.api

import pl.weblang.GoogleSettings
import pl.weblang.integration.web.HttpRequest
import pl.weblang.integration.web.google.convertToGoogleExactMatchQueryString

/**
 * Google API request object with default values
 */
class GoogleApiRequest(val searchedPhrase: List<String>,
                       val language: String = "lang_en",
                       val region: String = "countryUS",
                       val pageSize: Int = 10,
                       val customSearchEngineId: String = GoogleSettings.CUSTOM_SEARCH_ENGINE_ID,
                       val apiKey: String = GoogleSettings.API_KEY,
                       override val url: String = GOOGLE_API_URL) : HttpRequest {
    override val queryOptions: Map<String, Any>
        get() {
            val exactMatch = toExactMatch(searchedPhrase)
            return mapOf(Pair("q", exactMatch),
                    Pair("lr", language),
                    Pair("cr", region),
                    Pair("num", pageSize),
                    Pair("cx", customSearchEngineId),
                    Pair("key", apiKey))
        }

    private fun toExactMatch(query: List<String>) = query.convertToGoogleExactMatchQueryString()

}
