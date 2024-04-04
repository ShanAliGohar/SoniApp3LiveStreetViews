package com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class City {
    @SerializedName("id")
    @Expose
    var id = 0

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("coord")
    @Expose
    var coord: Coord? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("population")
    @Expose
    var population = 0

    @SerializedName("timezone")
    @Expose
    var timezone = 0

    @SerializedName("sunrise")
    @Expose
    var sunrise = 0

    @SerializedName("sunset")
    @Expose
    var sunset = 0
}
