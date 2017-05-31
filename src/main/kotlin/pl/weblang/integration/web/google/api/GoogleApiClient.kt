package pl.weblang.integration.web.google.api

import com.google.gson.Gson
import com.mashape.unirest.http.HttpResponse
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

        logger.info { "Google API call.\n [Request]:\n ${request.log()},\n [Response]:\n ${response.log()}" }

        return response.body
    }
}

private fun <T> HttpResponse<T>.log(): String {
    return """Status: ${this.status}
              Headers: ${this.headers}
              Body: ${this.body}
           """.trimIndent()
}

private fun com.mashape.unirest.request.HttpRequest.log(): String {
    return """Method: ${this.httpMethod}
              Url: ${this.url}
              Headers: ${this.headers}
           """.trimIndent()
}

