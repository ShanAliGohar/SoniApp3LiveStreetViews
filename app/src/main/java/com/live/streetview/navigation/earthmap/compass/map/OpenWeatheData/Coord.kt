package com.live.streetview.navigation.earthmap.compass.map.OpenWeatheData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Coord {
    @SerializedName("lon")
    @Expose
    var lon = 0.0
        private set

    @SerializedName("lat")
    @Expose
    var lat = 0.0
        private set

    fun setLon(lon: Int) {
        this.lon = lon.toDouble()
    }

    fun setLat(lat: Int) {
        this.lat = lat.toDouble()
    }
}
