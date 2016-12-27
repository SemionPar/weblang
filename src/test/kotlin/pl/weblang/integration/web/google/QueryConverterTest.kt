package pl.weblang.integration.web.google

import io.kotlintest.specs.FlatSpec

class QueryConverterTest : FlatSpec() {

    init {
        "Query converter" should "convert list of words to exact match query string" {
            val input = listOf("man", "in", "the", "box")
            val expected = "\"man+in+the+box\""
            toExactMatch(input) shouldEqual expected
        }
    }

}

