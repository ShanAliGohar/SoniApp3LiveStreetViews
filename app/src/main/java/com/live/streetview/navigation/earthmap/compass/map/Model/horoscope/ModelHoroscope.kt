package com.live.streetview.navigation.earthmap.compass.map.Model.horoscope

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ModelHoroscope {
    @SerializedName("date_range")
    @Expose
    var dateRange: String? = null

    @SerializedName("current_date")
    @Expose
    var currentDate: String? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("compatibility")
    @Expose
    var compatibility: String? = null

    @SerializedName("mood")
    @Expose
    var mood: String? = null

    @SerializedName("color")
    @Expose
    var color: String? = null

    @SerializedName("lucky_number")
    @Expose
    var luckyNumber: String? = null

    @SerializedName("lucky_time")
    @Expose
    var luckyTime: String? = null
}
