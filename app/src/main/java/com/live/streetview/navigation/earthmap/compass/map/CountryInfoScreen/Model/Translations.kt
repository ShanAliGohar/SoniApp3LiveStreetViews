package com.gps.navigation.streetview.earthmap.satelliteview.compass.pro

import com.google.gson.annotations.SerializedName

   
data class Translations (

   @SerializedName("de") var de : String,
   @SerializedName("es") var es : String,
   @SerializedName("fr") var fr : String,
   @SerializedName("ja") var ja : String,
   @SerializedName("it") var it : String,
   @SerializedName("br") var br : String,
   @SerializedName("pt") var pt : String,
   @SerializedName("nl") var nl : String,
   @SerializedName("hr") var hr : String,
   @SerializedName("fa") var fa : String

)