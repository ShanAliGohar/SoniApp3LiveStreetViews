package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Circle {
    @SerializedName("center")
    @Expose
    var center: Center? = null

    @SerializedName("radius")
    @Expose
    var radius: Int? = null
}
