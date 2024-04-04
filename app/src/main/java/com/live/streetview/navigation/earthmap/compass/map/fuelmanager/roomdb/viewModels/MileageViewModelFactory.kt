package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos.MileageLogRepo


class MileageViewModelFactory(private val repository: MileageLogRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MileageLogViewModel::class.java)) {
            return MileageLogViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}