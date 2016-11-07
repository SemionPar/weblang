package pl.weblang.integration.web.google

import com.mashape.unirest.http.Unirest

class GoogleServiceUnirest {
    fun search(query: String): String {
        return Unirest.get("$GOOGLE_BASE_URL/search").queryString("q", query).asString().body
    }
}
