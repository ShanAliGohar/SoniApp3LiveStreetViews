package com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Language : Serializable {
    @SerializedName("iso639_1")
    @Expose
    var iso6391: String? = null

    @SerializedName("iso639_2")
    @Expose
    var iso6392: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("nativeName")
    @Expose
    var nativeName: String? = null
}
