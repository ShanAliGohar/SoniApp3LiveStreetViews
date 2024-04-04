package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces

import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.api.NearByPlacesApi
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.Result

class NearbyPlacesRepository(private val api: NearByPlacesApi) {

    suspend fun getNearbyPlaces(latitude: Double, longitude: Double): List<Result> {
        val limit = 50
        val radius = 10000
        val authKey = "fsq3ir7AfjEeM+hEIHUmbNcWfYavseYaCeWBTebXZY1J67k="
        val latLng = "$latitude,$longitude"

        val response = api.getNearbyPlaces(latLng, limit, radius, authKey, "19007")
        if (response.isSuccessful) {
            return response.body()?.results ?: emptyList()
        } else {
            throw Exception("Failed to fetch nearby places")
        }
    }
}
