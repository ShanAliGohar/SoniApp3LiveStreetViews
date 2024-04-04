package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Context {
    @SerializedName("geo_bounds")
    @Expose
    var geoBounds: GeoBounds? = null
}
