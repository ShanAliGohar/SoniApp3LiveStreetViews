package com.live.streetview.navigation.earthmap.compass.map.fuelWork.dataClasses.fuelsModel

import com.google.gson.annotations.SerializedName

data class NewData(
    @SerializedName("Diesel prices")
    var dieselPrices: DieselPrices,
    @SerializedName("Gasoline prices")
    var gasolinePrices: GasolinePrices,
    @SerializedName("Methane prices")
    var methanePrices: MethanePrices,
    @SerializedName("Ethanol prices")
    var ethanolPrices: EthanolPrices,
    @SerializedName("Kerosene prices")
    var kerosenePrices: KerosenePrices,
    @SerializedName("Heating Oil prices")
    var heatingOilPrices: HeatingOilPrices,
    @SerializedName("LPG prices")
    var lpgPrices: LpgPrices
)
