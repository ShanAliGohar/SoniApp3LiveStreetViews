package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro

import com.google.gson.annotations.SerializedName

   
data class Languages (

   @SerializedName("iso639_1") var iso6391 : String,
   @SerializedName("iso639_2") var iso6392 : String,
   @SerializedName("name") var name : String,
   @SerializedName("nativeName") var nativeName : String

)