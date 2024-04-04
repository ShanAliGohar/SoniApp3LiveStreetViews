package com.live.streetview.navigation.earthmap.compass.map.OpenWeatheData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CurrentWeatherData {
    @SerializedName("coord")
    @Expose
    var coord: Coord? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

    @SerializedName("base")
    @Expose
    var base: String? = null

    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("visibility")
    @Expose
    var visibility = 0

    @SerializedName("wind")
    @Expose
    var wind: Wind? = null

    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null

    @SerializedName("dt")
    @Expose
    var dt = 0

    @SerializedName("sys")
    @Expose
    var sys: Sys? = null

    @SerializedName("timezone")
    @Expose
    var timezone = 0

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("cod")
    @Expose
    var cod = 0
}
