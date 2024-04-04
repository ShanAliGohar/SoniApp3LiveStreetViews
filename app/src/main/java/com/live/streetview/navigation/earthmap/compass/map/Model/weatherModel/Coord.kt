package com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Coord {
    @SerializedName("lat")
    @Expose
    var lat = 0.0

    @SerializedName("lon")
    @Expose
    var lon = 0.0
}
