package com.live.streetview.navigation.earthmap.compass.map.places


import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    companion object {
        //        const val Base_url = "https://api.pexels.com"
        const val Base_url = "https://photon.komoot.io"
    }

    @GET("/api/")
    suspend fun getPlaces(
        @Query("q") q: String
    ): MainPlacesModel

}