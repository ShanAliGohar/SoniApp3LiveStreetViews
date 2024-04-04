package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.NearbyPlacesRepository
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.nearbyplaces.pogoclasses.Result
import kotlinx.coroutines.launch


class NearbyPlacesViewModel(private val repository: NearbyPlacesRepository) : ViewModel() {

    private val _nearbyPlaces = MutableLiveData<List<Result>>()
    val nearbyPlaces: LiveData<List<Result>> get() = _nearbyPlaces

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun getNearbyPlaces(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            try {
                val places = repository.getNearbyPlaces(latitude, longitude)
                _nearbyPlaces.postValue(places)
            } catch (e: Exception) {
                _error.postValue(e.message)
            }
        }
    }
}
