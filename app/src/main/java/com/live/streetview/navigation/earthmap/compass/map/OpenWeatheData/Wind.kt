package com.live.streetview.navigation.earthmap.compass.map.OpenWeatheData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Wind {
    @SerializedName("speed")
    @Expose
    var speed = 0.0

    @SerializedName("deg")
    @Expose
    var deg = 0

    @SerializedName("gust")
    @Expose
    var gust = 0.0
}
