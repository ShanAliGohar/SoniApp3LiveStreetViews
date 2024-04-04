package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Player {
    @SerializedName("live")
    @Expose
    var live: Live? = null

    @SerializedName("day")
    @Expose
    var day: Day? = null

    @SerializedName("month")
    @Expose
    var month: Month? = null

    @SerializedName("year")
    @Expose
    var year: Year? = null

    @SerializedName("lifetime")
    @Expose
    var lifetime: Lifetime? = null
}
