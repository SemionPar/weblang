package pl.weblang.integration.web.google

import io.kotlintest.specs.FlatSpec

class GoogleServiceUnirestTest : FlatSpec() {

    init {
        val service = GoogleServiceUnirest()

        "GoogleServiceUnirestTest" should "make GET request to Google" {
            val page = service.search("pets")
            print(page)
            page shouldBe contain("Google")
        }
    }

}
