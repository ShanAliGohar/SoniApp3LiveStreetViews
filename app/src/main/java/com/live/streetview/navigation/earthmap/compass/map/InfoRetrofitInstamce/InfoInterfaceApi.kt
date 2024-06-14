package com.live.streetview.navigation.earthmap.compass.map.InfoRetrofitInstamce

import com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel.AllCountryDataModl
import retrofit2.Call
import retrofit2.http.GET

interface InfoInterfaceApi {
    @get:GET("v3.1/all")
    val data: Call<ArrayList<AllCountryDataModl?>?>?

    companion object {
        const val BASE_URL = "https://restcountries.com/"
    }
}
