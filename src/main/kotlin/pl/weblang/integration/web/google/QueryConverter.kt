package pl.weblang.integration.web.google

fun toExactMatch(query: List<String>) = query.convertToGoogleExactMatchQueryString()

private fun List<String>.convertToGoogleExactMatchQueryString(): String {
    return "\"".plus(this.reduce { s1, s2 -> s1 + "+" + s2 }).plus("\"")
}
