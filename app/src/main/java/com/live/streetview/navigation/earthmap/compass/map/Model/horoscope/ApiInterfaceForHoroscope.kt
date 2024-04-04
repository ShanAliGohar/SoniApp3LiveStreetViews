package com.live.streetview.navigation.earthmap.compass.map.Model.horoscope

import retrofit2.Call
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterfaceForHoroscope {
    @POST("/?")
    fun getHoroscope(
        @Query("sign") sign: String?,
        @Query("day") today: String?
    ): Call<ModelHoroscope?>?
}
