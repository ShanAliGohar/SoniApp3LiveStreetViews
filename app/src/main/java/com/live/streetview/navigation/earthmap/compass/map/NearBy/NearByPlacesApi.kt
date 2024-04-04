package com.live.streetview.navigation.earthmap.compass.map.NearBy

import com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass.ModelNearBy
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NearByPlacesApi {
    @GET("v3/places/search")
    fun getForCast(
        @Query("ll") ll: String?,
        @Query("client_id") client_id: String?,
        @Query("client_secret") client_secret: String?,
        @Query("v") v: String?,
        @Query("limit") limit: Int,
        @Query("radius") radius: Int,
        @Query("categories") categories: String?,
        @Header("Authorization") token: String?
    ): Call<ModelNearBy?>?
}