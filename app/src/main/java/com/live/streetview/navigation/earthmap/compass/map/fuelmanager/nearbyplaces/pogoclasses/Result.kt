package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses

import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.Category
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.Geocodes
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.Location
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.RelatedPlaces

data class Result(
    val categories: List<Category>,
    val chains: List<Any>,
    val distance: Int,
    val fsq_id: String,
    val geocodes: Geocodes,
    val link: String,
    val location: Location,
    val name: String,
    val related_places: RelatedPlaces,
    val timezone: String
)