package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Location {
    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("country")
    @Expose
    var country: String? = null

    @SerializedName("cross_street")
    @Expose
    var crossStreet: String? = null

    @SerializedName("formatted_address")
    @Expose
    var formattedAddress: String? = null

    @SerializedName("locality")
    @Expose
    var locality: String? = null

    @SerializedName("postcode")
    @Expose
    var postcode: String? = null

    @SerializedName("region")
    @Expose
    var region: String? = null
}
