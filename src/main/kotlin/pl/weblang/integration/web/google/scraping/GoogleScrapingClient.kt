package pl.weblang.integration.web.google.scraping

import com.mashape.unirest.http.Unirest
import mu.KLogging
import pl.weblang.integration.web.HttpRequest
import pl.weblang.integration.web.UnsuccessfulRestCallException

class GoogleScrapingClient() : Client {

    companion object : KLogging()

    override fun search(httpRequest: HttpRequest): String {
        val request = Unirest.get(httpRequest.url).queryString(httpRequest.queryOptions).header("User-Agent",
                                                                                                Client.client.getRandomUserAgent())
        val response = request.asString()
        val status = response.status
        if (status != 200) {
            logger.warn { "Unsuccessful call. Request: $request, Response: $response" }
            throw UnsuccessfulRestCallException("Status: $status, url: ${httpRequest.url}?${httpRequest.queryOptions}")
        }
        logger.debug { "Google call. Request: $request, Response: $response" }
        return response.body
    }
}

