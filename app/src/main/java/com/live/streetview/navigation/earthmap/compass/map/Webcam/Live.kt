package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Live {
    @SerializedName("available")
    @Expose
    var available: Boolean? = null

    @SerializedName("embed")
    @Expose
    var embed: String? = null
}
