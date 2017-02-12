package pl.weblang.background

class SubgroupTokenizer(val subgroupDelimiters: Array<String> = arrayOf("\"", "\'", ".", "(", ")", ";", ":")) {

    fun tokenizeToSubgroups(sentence: String,
                            vararg delimiters: String = subgroupDelimiters): List<String> {
        return sentence.split(*delimiters).filterNot(String::isEmpty)
    }

}
