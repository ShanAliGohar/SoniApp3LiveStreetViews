package com.live.streetview.navigation.earthmap.compass.map.activities.ModelClass

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Result {
    @SerializedName("fsq_id")
    @Expose
    var fsqId: String? = null

    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null

    @SerializedName("chains")
    @Expose
    var chains: List<Any>? = null

    @SerializedName("distance")
    @Expose
    var distance: Int? = null

    @SerializedName("geocodes")
    @Expose
    var geocodes: Geocodes? = null

    @SerializedName("location")
    @Expose
    var location: Location? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("related_places")
    @Expose
    var relatedPlaces: RelatedPlaces? = null

    @SerializedName("timezone")
    @Expose
    var timezone: String? = null
}
