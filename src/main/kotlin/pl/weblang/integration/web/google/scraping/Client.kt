package pl.weblang.integration.web.google.scraping

import pl.weblang.integration.web.HttpRequest

interface Client {
    fun search(httpRequest: HttpRequest): String
}
