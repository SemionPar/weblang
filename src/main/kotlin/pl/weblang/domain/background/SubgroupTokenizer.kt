package pl.weblang.domain.background

/**
 * Separates segment to subgroups according to provided delimiters
 */
class SubgroupTokenizer(private val subgroupDelimiters: Array<String> = arrayOf("\"", "\'", ".", "(", ")", ";", ":")) {

    fun tokenizeToSubgroups(sentence: String,
                            vararg delimiters: String = subgroupDelimiters): List<String> {
        return sentence.split(*delimiters).filterNot(String::isEmpty)
    }

}
