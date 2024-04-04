package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.fuelsModel

import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.fuelsModel.Business
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses.fuelsModel.Households

data class NaturalGasPrices(
    val Business: Business,
    val Households: Households
)