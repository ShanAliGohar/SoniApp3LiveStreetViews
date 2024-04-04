package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Icon {
    @SerializedName("prefix")
    @Expose
    var prefix: String? = null

    @SerializedName("suffix")
    @Expose
    var suffix: String? = null
}
