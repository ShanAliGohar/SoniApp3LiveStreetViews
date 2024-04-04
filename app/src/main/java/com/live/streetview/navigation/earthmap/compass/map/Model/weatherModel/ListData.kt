package com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ListData {
    @SerializedName("dt")
    @Expose
    var dt = 0

    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

    @SerializedName("clouds")
    @Expose
    var clouds: Clouds? = null

    @SerializedName("wind")
    @Expose
    var wind: Wind? = null

    @SerializedName("visibility")
    @Expose
    var visibility = 0

    @SerializedName("pop")
    @Expose
    var pop: Double? = null

    @SerializedName("rain")
    @Expose
    var rain: Rain? = null

    @SerializedName("sys")
    @Expose
    var sys: Sys? = null

    @SerializedName("dt_txt")
    @Expose
    var dtTxt: String? = null
}
