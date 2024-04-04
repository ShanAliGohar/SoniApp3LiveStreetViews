package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Image {
    @SerializedName("current")
    @Expose
    var current: Current? = null

    @SerializedName("sizes")
    @Expose
    var sizes: Sizes? = null

    @SerializedName("daylight")
    @Expose
    var daylight: Daylight? = null

    @SerializedName("update")
    @Expose
    var update: Int? = null
}
