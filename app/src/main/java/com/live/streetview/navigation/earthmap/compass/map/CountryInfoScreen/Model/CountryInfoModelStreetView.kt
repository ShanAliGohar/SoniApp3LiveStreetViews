package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro

import com.google.gson.annotations.SerializedName
import kotlin.collections.List
   
data class CountryInfoModelStreetView (

   @SerializedName("name") var name : String,
   @SerializedName("topLevelDomain") var topLevelDomain : List<String>,
   @SerializedName("alpha2Code") var alpha2Code : String,
   @SerializedName("alpha3Code") var alpha3Code : String,
   @SerializedName("callingCodes") var callingCodes : List<String>,
   @SerializedName("capital") var capital : String,
   @SerializedName("altSpellings") var altSpellings : List<String>,
   @SerializedName("region") var region : String,
   @SerializedName("subregion") var subregion : String,
   @SerializedName("population") var population : Int,
   @SerializedName("latlng") var latlng : List<Float>,
   @SerializedName("demonym") var demonym : String,
   @SerializedName("area") var area : Float,
   @SerializedName("gini") var gini : Double,
   @SerializedName("timezones") var timezones : List<String>,
   @SerializedName("borders") var borders : List<String>,
   @SerializedName("nativeName") var nativeName : String,
   @SerializedName("numericCode") var numericCode : String,
   @SerializedName("currencies") var currencies : List<Currencies>,
   @SerializedName("languages") var languages : List<Languages>,
   @SerializedName("translations") var translations : Translations,
   @SerializedName("flag") var flag : String,
   @SerializedName("regionalBlocs") var regionalBlocs : List<RegionalBlocs>,
   @SerializedName("cioc") var cioc : String

)