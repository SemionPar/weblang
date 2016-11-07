package pl.weblang.integration.web

import okhttp3.OkHttpClient
import pl.weblang.integration.web.google.GOOGLE_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitServiceGenerator {

    companion object {

        private val httpClient = OkHttpClient.Builder()
        private val builder = Retrofit.Builder().baseUrl(GOOGLE_BASE_URL).addConverterFactory(GsonConverterFactory.create())

        fun <T> createService(serviceClass: Class<T>): T {
            val retrofit = builder.client(httpClient.build()).build()
            return retrofit.create(serviceClass)
        }
    }


}

