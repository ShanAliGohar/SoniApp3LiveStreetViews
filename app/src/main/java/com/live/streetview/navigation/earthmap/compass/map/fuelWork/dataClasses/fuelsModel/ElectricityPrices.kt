package com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.fuelsModel

import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.fuelsModel.Business
import com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.fuelsModel.Households

data class ElectricityPrices(
    val Business: Business,
    val Households: Households
)