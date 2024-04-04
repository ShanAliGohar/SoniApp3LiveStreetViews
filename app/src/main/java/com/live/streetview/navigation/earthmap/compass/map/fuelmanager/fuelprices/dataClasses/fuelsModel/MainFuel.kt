package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.fuelsModel

import com.google.gson.annotations.SerializedName

data class MainFuel(
    val country: String,
    @SerializedName("data")
    val data: NewData,
    val electricityPrices: ElectricityPrices,
    val naturalGasPrices: NaturalGasPrices
)