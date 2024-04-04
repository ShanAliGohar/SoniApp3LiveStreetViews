package com.live.streetview.navigation.earthmap.compass.map.FM

import com.live.streetview.navigation.earthmap.compass.map.fmmodel.CountryFMModelInformation
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface FMApiInterface {
    @GET("/json/stations/bycountry/{countryName}")
    fun getFM(@Path("countryName") countryName: String?): Call<ArrayList<CountryFMModelInformation?>?>?

    @get:GET("/json/countries")
    val countries: Call<ArrayList<CountryFMModelInformation?>?>?
}
