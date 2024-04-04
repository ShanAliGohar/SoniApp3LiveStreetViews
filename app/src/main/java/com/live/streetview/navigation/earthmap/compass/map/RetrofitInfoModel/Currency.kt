package com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Currency : Serializable {
    @SerializedName("code")
    @Expose
    var code: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("symbol")
    @Expose
    var symbol: String? = null
}
