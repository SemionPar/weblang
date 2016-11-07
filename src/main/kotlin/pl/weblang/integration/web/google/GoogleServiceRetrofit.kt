package pl.weblang.integration.web.google

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleClientRetrofit {
    @GET("/search") fun search(@Query("q") queryString: String): Call<String>
}

class GoogleServiceRetrofit(val client: GoogleClientRetrofit) {

    fun search(query: String): String {
        val call = client.search(query)
        return call.execute().body()
    }
}
