package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos.VehicleRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class VehicleViewModel(var repository: VehicleRepo) : ViewModel() {
    fun updateBiFuelState(vehicleId: Long, isBiFuel: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateBiFuelState(vehicleId, isBiFuel)
        }
    }

     fun getAllVehicles() = repository.getAllVehicles()


}