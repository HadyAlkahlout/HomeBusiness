package com.nurbk.ps.homebusness.newwork


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {


    companion object {
        private const val BASE_URL = "https://hb-app.net/api/"
        private var instance: RetrofitInstance? = null
        var api: Api? = null

        init {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            api = retrofit.create(Api::class.java)

        }

         fun getInstance(): RetrofitInstance {
            if (instance == null) {
                instance = getInstance()
            }
            return instance!!
        }


    }

}