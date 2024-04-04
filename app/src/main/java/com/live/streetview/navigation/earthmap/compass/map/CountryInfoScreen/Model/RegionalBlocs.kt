package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro

import com.google.gson.annotations.SerializedName
import kotlin.collections.List


data class RegionalBlocs (

   @SerializedName("acronym") var acronym : String,
   @SerializedName("name") var name : String,
   @SerializedName("otherAcronyms") var otherAcronyms : List<String>,
   @SerializedName("otherNames") var otherNames : List<String>

)