package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos.VehicleRepo


class VehicleViewModelFactory(private val repository: VehicleRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(VehicleViewModel::class.java)) {
            return VehicleViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}