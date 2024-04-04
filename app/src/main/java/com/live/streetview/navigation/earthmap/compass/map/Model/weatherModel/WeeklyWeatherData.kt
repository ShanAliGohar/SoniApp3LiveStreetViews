package com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WeeklyWeatherData {
    @SerializedName("cod")
    @Expose
    var cod: String? = null

    @SerializedName("message")
    @Expose
    var message = 0

    @SerializedName("cnt")
    @Expose
    var cnt = 0

    @SerializedName("list")
    @Expose
    var listData: List<ListData>? = null

    @SerializedName("city")
    @Expose
    var city: City? = null
}
