package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.retrofit

import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.Data
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.fuelsModel.MainFuel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface APIWork {
    @GET("getPrices?")
    suspend fun getPlaces(
        @Query("country") q: String
    ): Data

    @GET("getPrices?")
     fun getPlaces2(
        @Query("country") q: String
    ): Call<MainFuel>

}