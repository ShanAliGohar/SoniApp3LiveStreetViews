package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.repos

import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle.VehicleDao

class VehicleRepo(private val vehicleDao: VehicleDao) {
    suspend fun updateBiFuelState(vehicleId: Long, isBiFuel: Boolean) {
        vehicleDao.updateBiFuelState(vehicleId, isBiFuel)
    }

     fun getAllVehicles() = vehicleDao.getAllVehicleData()
}