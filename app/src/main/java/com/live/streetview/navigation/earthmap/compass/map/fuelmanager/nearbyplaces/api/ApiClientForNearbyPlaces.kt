package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClientForNearbyPlaces {

    private const val BASE_URL_FOURSQUARE = "https://api.foursquare.com/"
    private var retrofitFoursquare: Retrofit? = null

    val retrofitInstance: Retrofit
        get() {
            return retrofitFoursquare ?: synchronized(this) {
                retrofitFoursquare ?: createRetrofit().also {
                    retrofitFoursquare = it
                }
            }
        }

    private fun createRetrofit(): Retrofit {
        val builder = OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

        return Retrofit.Builder()
            .baseUrl(BASE_URL_FOURSQUARE)
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }
}
