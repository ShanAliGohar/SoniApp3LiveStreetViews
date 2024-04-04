package com.live.streetview.navigation.earthmap.compass.map.OpenWeatheData

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Sys {
    @SerializedName("type")
    @Expose
    var type = 0

    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("sunrise")
    @Expose
    var sunrise = 0

    @SerializedName("sunset")
    @Expose
    var sunset = 0
}
