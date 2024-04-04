package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelNearBy {
    @SerializedName("results")
    @Expose
    var results: List<Result>? = null

    @SerializedName("context")
    @Expose
    var context: Context? = null
}
