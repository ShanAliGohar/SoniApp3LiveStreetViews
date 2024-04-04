package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("offset")
    @Expose
    var offset: Int? = null

    @SerializedName("limit")
    @Expose
    var limit: Int? = null

    @SerializedName("total")
    @Expose
    var total: Int? = null

    @SerializedName("webcams")
    @Expose
    var webcams: List<Webcam>? = null
}
