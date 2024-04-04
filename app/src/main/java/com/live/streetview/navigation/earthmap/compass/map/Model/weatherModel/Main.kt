package com.live.streetview.navigation.earthmap.compass.map.Model.weatherModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Main {
    @SerializedName("temp")
    @Expose
    var temp = 0.0

    @SerializedName("feels_like")
    @Expose
    var feelsLike = 0.0

    @SerializedName("temp_min")
    @Expose
    var tempMin = 0.0

    @SerializedName("temp_max")
    @Expose
    var tempMax = 0.0

    @SerializedName("pressure")
    @Expose
    var pressure = 0

    @SerializedName("sea_level")
    @Expose
    var seaLevel = 0

    @SerializedName("grnd_level")
    @Expose
    var grndLevel = 0

    @SerializedName("humidity")
    @Expose
    var humidity = 0

    @SerializedName("temp_kf")
    @Expose
    var tempKf = 0.0
}
