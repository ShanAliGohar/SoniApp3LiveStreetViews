package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeoBounds {
    @SerializedName("circle")
    @Expose
    var circle: Circle? = null
}
