package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Current {
    @SerializedName("icon")
    @Expose
    var icon: String? = null

                    @SerializedName("thumbnail")
    @Expose
    var thumbnail: String? = null

    @SerializedName("preview")
    @Expose
    var preview: String? = null

    @SerializedName("toenail")
    @Expose
    var toenail: String? = null
}
