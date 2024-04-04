package com.live.streetview.navigation.earthmap.compass.map.FM

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object FMRetrofitInstance {
    private const val BASE_URL = " https://nl1.api.radio-browser.info/"
    private const val BASE_URL_PLACES = " https://photon.komoot.io"
    private var retrofitWeatherApi: Retrofit? = null
    val retrofitInstance: Retrofit?
        get() {
            if (retrofitWeatherApi == null) {
                val builder =  OkHttpClient.Builder()
                builder.connectTimeout(30, TimeUnit.SECONDS)
                builder.readTimeout(30, TimeUnit.SECONDS)
                builder.writeTimeout(30, TimeUnit.SECONDS)
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
                retrofitWeatherApi = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
            }
            return retrofitWeatherApi
        }
    val retrofitPlacesInstance: Retrofit?
        get() {
            if (retrofitWeatherApi == null) {
                val builder =  OkHttpClient.Builder()
                builder.connectTimeout(30, TimeUnit.SECONDS)
                builder.readTimeout(30, TimeUnit.SECONDS)
                builder.writeTimeout(30, TimeUnit.SECONDS)
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
                retrofitWeatherApi = Retrofit.Builder()
                    .baseUrl(BASE_URL_PLACES)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
            }
            return retrofitWeatherApi
        }
}
