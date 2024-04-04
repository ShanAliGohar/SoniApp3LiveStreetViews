package com.live.streetview.navigation.earthmap.compass.map.fmmodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CountryFMModelInformation {
    @SerializedName("changeuuid")
    @Expose
    var changeuuid: String? = null

    @SerializedName("stationuuid")
    @Expose
    var stationuuid: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("url")
    @Expose
    var url: String? = null

    @SerializedName("url_resolved")
    @Expose
    var urlResolved: String? = null

    @SerializedName("homepage")
    @Expose
    var homepage: String? = null

    @SerializedName("favicon")
    @Expose
    var favicon: String? = null

    @SerializedName("tags")
    @Expose
    var tags: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("countrycode")
    @Expose
    var countrycode: String? = null

    @SerializedName("state")
    @Expose
    var state: String? = null

    @SerializedName("language")
    @Expose
    var language: String? = null

    @SerializedName("votes")
    @Expose
    var votes: Int? = null

    @SerializedName("lastchangetime")
    @Expose
    var lastchangetime: String? = null

    @SerializedName("codec")
    @Expose
    var codec: String? = null

    @SerializedName("bitrate")
    @Expose
    var bitrate: Int? = null

    @SerializedName("hls")
    @Expose
    var hls: Int? = null

    @SerializedName("lastcheckok")
    @Expose
    var lastcheckok: Int? = null

    @SerializedName("lastchecktime")
    @Expose
    var lastchecktime: String? = null

    @SerializedName("lastcheckoktime")
    @Expose
    var lastcheckoktime: String? = null

    @SerializedName("lastlocalchecktime")
    @Expose
    var lastlocalchecktime: String? = null

    @SerializedName("clicktimestamp")
    @Expose
    var clicktimestamp: String? = null

    @SerializedName("clickcount")
    @Expose
    var clickcount: Int? = null

    @SerializedName("clicktrend")
    @Expose
    var clicktrend: Int? = null
}
