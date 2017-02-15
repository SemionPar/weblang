package pl.weblang.integration.web.google.api

import com.google.gson.Gson
import com.mashape.unirest.http.ObjectMapper
import com.mashape.unirest.http.Unirest
import mu.KLogging
import org.apache.http.HttpHeaders
import org.apache.http.entity.ContentType
import pl.weblang.integration.web.HttpRequest
import pl.weblang.integration.web.InstantSearchResponse
import pl.weblang.integration.web.UnsuccessfulRestCallException

/**
 * REST client Google API
 */
class GoogleApiClient {

    companion object : KLogging()

    /**
     * Setup an object mapper within Unirest so it could deserialize InstantSearchResponse
     */
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

    /**
     * Perform search with given request parameters
     */
    fun search(httpRequest: HttpRequest): InstantSearchResponse.GoogleApiInstantSearchResponse {
        val request = Unirest.get(httpRequest.url)
                .queryString(httpRequest.queryOptions)
                .header(HttpHeaders.ACCEPT, ContentType.APPLICATION_JSON.mimeType)

        val response = request.asObject(InstantSearchResponse.GoogleApiInstantSearchResponse::class.java)

        if (response.status != 200) {
            logger.warn { "Unsuccessful call. Request: $request, Response: $response" }
            throw UnsuccessfulRestCallException(
                    "Status: ${response.status}, url: ${httpRequest.url}, query: ${httpRequest.queryOptions}")
        }

        if (response.body == null) {
            logger.warn { "Unsuccessful call. Response is null" }
            throw UnsuccessfulRestCallException(
                    "Response null, url: ${httpRequest.url}, query: ${httpRequest.queryOptions}")
        }

        logger.debug { "Google API call. Request: $request, Response: $response" }

        return response.body
    }
}

