package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.fuelprices.dataClasses

import com.google.gson.annotations.SerializedName

//data class Data(
//    val country: String,
//    @SerializedName("Gasoline prices")
//    val GasolinePrices: GasolinePrices,
//    @SerializedName("Diesel prices")
//    val DieselPrices: DieselPrices,
//    @SerializedName("Ethanol prices")
//    val EthanolPrices: EthanolPrices
//)

data class Root(
    val country: String,
    val data: Data,
    val electricityPrices: ElectricityPrices,
    val naturalGasPrices: NaturalGasPrices,
)

data class Data(
    @SerializedName("Gasoline prices")
    val gasolinePrices: GasolinePrices,
    @SerializedName("Diesel prices")
    val dieselPrices: DieselPrices,
    @SerializedName("Ethanol prices")
    val ethanolPrices: EthanolPrices,
)

data class GasolinePrices(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

data class DieselPrices(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

data class EthanolPrices(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

data class ElectricityPrices(
    @SerializedName("Households")
    val households: Households,
    @SerializedName("Business")
    val business: Business,
)

data class Households(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

data class Business(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

data class NaturalGasPrices(
    @SerializedName("Households")
    val households: Households2,
    @SerializedName("Business")
    val business: Business2,
)

data class Households2(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

data class Business2(
    @SerializedName("Date")
    val date: String,
    @SerializedName("Currency")
    val currency: String,
    @SerializedName("USD")
    val usd: String,
)

