package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Preview {
             @SerializedName("width")
    @Expose
    var width: Int? = null

    @SerializedName("height")
    @Expose
    var height: Int? = null
}
