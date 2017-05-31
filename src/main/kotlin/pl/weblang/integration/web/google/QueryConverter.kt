package pl.weblang.integration.web.google

fun toExactMatch(query: String) = query.convertToGoogleExactMatchQueryString()

fun String.convertToGoogleExactMatchQueryString(): String {
    return "\"".plus(this.replace(" ", "+")).plus("\"")
}
