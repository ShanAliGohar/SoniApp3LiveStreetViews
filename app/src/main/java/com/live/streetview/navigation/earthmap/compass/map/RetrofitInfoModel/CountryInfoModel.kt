package com.live.streetview.navigation.earthmap.compass.map.RetrofitInfoModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CountryInfoModel : Serializable {
    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("topLevelDomain")
    @Expose
    var topLevelDomain: List<String>? = null

    @SerializedName("alpha2Code")
    @Expose
    var alpha2Code: String? = null

    @SerializedName("alpha3Code")
    @Expose
    var alpha3Code: String? = null

    @SerializedName("callingCodes")
    @Expose
    var callingCodes: String? = null

    @SerializedName("capital")
    @Expose
    var capital: String? = null

    @SerializedName("altSpellings")
    @Expose
    var altSpellings: List<String>? = null

    @SerializedName("subregion")
    @Expose
    var subregion: String? = null

    @SerializedName("region")
    @Expose
    var region: String? = null

    @SerializedName("population")
    @Expose
    var population = 0
    var lat: Double? = null
    var lng: Double? = null

    @SerializedName("demonym")
    @Expose
    var demonym: String? = null

    @SerializedName("area")
    @Expose
    var area = 0.0

    @SerializedName("timezones")
    @Expose
    var timezones: List<String>? = null

    @SerializedName("borders")
    @Expose
    var borders: List<String>? = null

    @SerializedName("nativeName")
    @Expose
    var nativeName: String? = null

    @SerializedName("numericCode")
    @Expose
    var numericCode: String? = null

    @SerializedName("flag")
    @Expose
    var flag: String? = null

    @SerializedName("cioc")
    @Expose
    var cioc: String? = null

    @SerializedName("independent")
    @Expose
    var isIndependent = false
}
