package com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Flags : Serializable {
    @SerializedName("svg")
    @Expose
    var svg: String? = null

    @SerializedName("png")
    @Expose
    var png: String? = null
}
