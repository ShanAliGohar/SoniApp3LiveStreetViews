package com.live.streetview.navigation.earthmap.compass.map.weatherinstance

import com.live.streetview.navigation.earthmap.compass.map.places.RetrofitService
import okhttp3.OkHttpClient

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstanceClass {
/*    const val urlOpenWeather = "https://api.openweathermap.org/data/2.5/"
    const val BASE_URLWeather5Days = "https://api.openweathermap.org/data/2.5/"*/
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
                    .baseUrl(OpenWeatherAPI.BASE_URLCurrentWeather)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
            }
            return retrofitWeatherApi
        }
    val weeklyWeatherRetrofitInstance: Retrofit?
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
                    .baseUrl(RetrofitService.Base_url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build()
            }
            return retrofitWeatherApi
        }
}
