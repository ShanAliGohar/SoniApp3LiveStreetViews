package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Center {
    @SerializedName("latitude")
    @Expose
    var latitude: Double? = null

    @SerializedName("longitude")
    @Expose
    var longitude: Double? = null
}
