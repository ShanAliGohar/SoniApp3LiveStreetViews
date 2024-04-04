package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos.MileageLogRepo
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity


class MileageLogViewModel(var repository: MileageLogRepo) : ViewModel() {
    val getAllMileageData: LiveData<List<MileageLogEntity>> = repository.getAllMileageData()


}