package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro

import com.google.gson.annotations.SerializedName

   
data class Currencies (

   @SerializedName("code") var code : String,
   @SerializedName("name") var name : String,
   @SerializedName("symbol") var symbol : String

)