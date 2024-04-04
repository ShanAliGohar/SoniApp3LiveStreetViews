package com.live.streetview.navigation.earthmap.compass.map.Webcam

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Sizes {
    @SerializedName("icon")
    @Expose
    var icon: Icon? = null

    @SerializedName("thumbnail")
    @Expose
    var thumbnail: Thumbnail? = null

    @SerializedName("preview")
    @Expose
    var preview: Preview? = null

    @SerializedName("toenail")
    @Expose
    var toenail: Toenail? = null
}
