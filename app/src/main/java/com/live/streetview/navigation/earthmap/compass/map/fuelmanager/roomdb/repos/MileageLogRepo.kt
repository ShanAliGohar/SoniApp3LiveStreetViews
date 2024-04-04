package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos

import androidx.lifecycle.LiveData
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity

class MileageLogRepo(private val mileageDao: MileageLogDao) {
    fun getAllMileageData(): LiveData<List<MileageLogEntity>> {
        return mileageDao.getAllMileageLogData()
    }


}