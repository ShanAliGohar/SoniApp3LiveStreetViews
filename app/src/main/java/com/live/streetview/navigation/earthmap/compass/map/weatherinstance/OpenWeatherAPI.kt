package com.live.streetview.navigation.earthmap.compass.map.weatherinstance

import com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel.WeeklyWeatherData
import com.live.streetview.navigation.earthmap.compass.map.OpenWeatheData.CurrentWeatherData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherAPI {
    @GET("weather")
    fun getCurrentWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") token: String?
    ): Call<CurrentWeatherData?>?

    @GET("forecast")
    fun getWeatherDataOf5Days(
        @Query("lat") lat: Double,
        @Query("lon") lng: Double,
        @Query("appid") token: String?
    ): Call<WeeklyWeatherData?>? /* @GET("data/2.5/forecast")
    Call<CurrentWeatherData> getForCast(@Query("q") String Cityname, @Query("appid") String apikey);*/

    companion object {
        const val BASE_URLCurrentWeather = "https://api.openweathermap.org/data/2.5/"
    }
}