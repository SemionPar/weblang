package pl.weblang.integration.web.google

class QueryConverter {
    companion object
}

fun toExactMatch(query: List<String>) = query.convertToGoogleExactMatchQueryString()

private fun List<String>.convertToGoogleExactMatchQueryString(): String {
    return "\"".plus(this.reduce { s1, s2 -> s1 + "+" + s2 }).plus("\"")
}
