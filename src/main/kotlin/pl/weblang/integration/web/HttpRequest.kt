package pl.weblang.integration.web

interface HttpRequest {
    val queryOptions: Map<String, Any>
    val url: String
}
