package com.live.streetview.navigation.earthmap.compass.map.Model.horoscope

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientForHoroscope {
    const val urlFoursquare = "https://aztro.sameerkumar.website"
    private var retrofitFoursquare: Retrofit? = null
    val retrofitInstance: Retrofit?
        get() {
            if (retrofitFoursquare == null) {
                try {
                    val builder =  OkHttpClient.Builder()
                    builder.connectTimeout(30, TimeUnit.SECONDS)
                    builder.readTimeout(30, TimeUnit.SECONDS)
                    builder.writeTimeout(30, TimeUnit.SECONDS)
                    val interceptor = HttpLoggingInterceptor()
                    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                    builder.addInterceptor(interceptor)
                    retrofitFoursquare = Retrofit.Builder()
                        .baseUrl(urlFoursquare)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(builder.build())
                        .build()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            return retrofitFoursquare
        }
}
