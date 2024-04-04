package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.api


import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.NearbyPlacesModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface NearByPlacesApi {

    @Headers("Accept: application/json")
    @GET("v3/places/search")
    suspend fun getNearbyPlaces(
        @Query("ll") latitudeLongitude: String,
        @Query("limit") limit: Int,
        @Query("radius") radius: Int,
        @Header("Authorization") authKey: String,
        @Query("categories") categories: String
    ): Response<NearbyPlacesModel>
}
