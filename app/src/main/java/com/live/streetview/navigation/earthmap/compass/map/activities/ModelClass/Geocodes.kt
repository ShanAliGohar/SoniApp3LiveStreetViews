package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Geocodes {
    @SerializedName("main")
    @Expose
    var main: Main? = null
}
