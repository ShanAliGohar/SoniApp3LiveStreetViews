package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class WebCamModel {
    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("result")
    @Expose
    var result: Result? = null
}
