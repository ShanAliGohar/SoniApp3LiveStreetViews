package com.live.streetview.navigation.earthmap.compass.map.InfoRetrofitInstamce

import com.live.streetview.navigation.earthmap.compass.map.InfoRetrofitInstamce.InfoInterfaceApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientforInfoCountryData private constructor() {
    val myApi: InfoInterfaceApi

    init {
        val retrofit = Retrofit.Builder().baseUrl(InfoInterfaceApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        myApi = retrofit.create(InfoInterfaceApi::class.java)
    }

    companion object {
        @get:Synchronized
        var instance: RetrofitClientforInfoCountryData? = null
            get() {
                if (field == null) {
                    field = RetrofitClientforInfoCountryData()
                }
                return field
            }
            private set
    }
}
