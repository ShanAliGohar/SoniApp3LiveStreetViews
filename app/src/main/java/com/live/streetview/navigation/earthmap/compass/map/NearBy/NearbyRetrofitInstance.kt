package com.live.streetview.navigation.earthmap.compass.map.NearBy

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NearbyRetrofitInstance {
    const val urlOpenWeather = "https://api.foursquare.com/"
    private var retrofitWeatherApi: Retrofit? = null
    val retrofitInstance: Retrofit?
        get() {
            if (retrofitWeatherApi == null) {
                val builder = OkHttpClient.Builder()
                builder.connectTimeout(30, TimeUnit.SECONDS)
                builder.readTimeout(30, TimeUnit.SECONDS)
                builder.writeTimeout(30, TimeUnit.SECONDS)
                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                builder.addInterceptor(interceptor)
                retrofitWeatherApi = Retrofit.Builder()
                    .baseUrl(urlOpenWeather)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
            }
            return retrofitWeatherApi
        }
}
