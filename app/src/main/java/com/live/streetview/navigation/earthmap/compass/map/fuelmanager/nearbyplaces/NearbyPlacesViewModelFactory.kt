package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class NearbyPlacesViewModelFactory(private val repository: NearbyPlacesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearbyPlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NearbyPlacesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
