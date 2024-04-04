package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Webcam {
    @SerializedName("id")
    @Expose
    var id: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null

    @SerializedName("title")
    @Expose
    var title: String? = null

    @SerializedName("image")
    @Expose
    var image: Image? = null

    @SerializedName("player")
    @Expose
    var player: Player? = null
}
