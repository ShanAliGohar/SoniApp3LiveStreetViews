package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.retrofit

import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClientFuel {
    companion object {
   //   private const val BASE_URL = "http://fuel.navigation.center/api/"
//
//        fun create(): APIWork {
//            return Retrofit.Builder()
//                .baseUrl(BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build().create(APIWork::class.java)
   private const val BASE_URL = "http://fuel.navigation.center/api/"
        val httpClient = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
        val apiService: APIWork by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(OkHttpClient.Builder().also { client ->
                    val logging = HttpLoggingInterceptor()
                    logging.setLevel(HttpLoggingInterceptor.Level.BODY)
                    client.addInterceptor(logging)
                }.build())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build()
            retrofit .create(APIWork::class.java)
        }

    }
}
