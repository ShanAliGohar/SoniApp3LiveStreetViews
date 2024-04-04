package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoScreen.Interfaces


import com.gps.navigation.streetview.earthmap.satelliteview.compass.pro.CountryInfoModelStreetView
import retrofit2.Call
import retrofit2.http.GET


interface CountryInfoInterfaceStreetView {
    @GET("/v2/all")
    fun getGeoResults(
    ): Call<List<CountryInfoModelStreetView>>?
}
