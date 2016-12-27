package pl.weblang.integration.web.google.api

import com.google.gson.Gson
import com.mashape.unirest.http.ObjectMapper
import com.mashape.unirest.http.Unirest
import mu.KLogging
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType
import pl.weblang.integration.web.HttpRequest
import pl.weblang.integration.web.SearchResponse
import pl.weblang.integration.web.UnsuccessfulRestCallException

class GoogleApiClient {
    companion object : KLogging()

    init {
        Unirest.setObjectMapper(object : ObjectMapper {

            val mapper: Gson = Gson()

            override fun writeValue(value: Any?): String {
                return mapper.toJson(value)
            }

            override fun <T : Any?> readValue(value: String?, valueType: Class<T>?): T {
                return mapper.fromJson(value, valueType)
            }
        })
    }

    fun search(httpRequest: HttpRequest): SearchResponse.GoogleApiSearchResponse {
        val request = Unirest.get(httpRequest.url).queryString(httpRequest.queryOptions).header(HttpHeaders.ACCEPT,
                                                                                                ContentType.APPLICATION_JSON.mimeType)
        val response = request.asObject(SearchResponse.GoogleApiSearchResponse::class.java)
        if (response.status != 200) {
            logger.warn { "Unsuccessful call. Request: $request, Response: $response" }
            throw UnsuccessfulRestCallException("Status: ${response.status}, url: ${httpRequest.url}, query: ${httpRequest.queryOptions}")
        }
        logger.debug { "Google API call. Request: $request, Response: $response" }
        return response.body
    }
}

