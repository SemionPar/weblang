package pl.weblang.integration.web.google

import io.kotlintest.specs.FlatSpec
import pl.weblang.integration.web.RetrofitServiceGenerator

class GoogleServiceRetrofitTest : FlatSpec() {

    init {
        val service = GoogleServiceRetrofit(RetrofitServiceGenerator.createService(GoogleClientRetrofit::class.java))

        "GoogleServiceRetrofit" should "make GET request to Google" {
            val page = service.search("pets")
            page shouldBe contain("Google")
        }
    }

}
