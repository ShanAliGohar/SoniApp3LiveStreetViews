package com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class RegionalBloc : Serializable {
    @SerializedName("acronym")
    @Expose
    var acronym: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null
}
