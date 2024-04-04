package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface VehicleDao {
    @Insert
    suspend fun insertVehicleData(vehicle: VehicleEntity): Long // Note the return type Long

    @Query("SELECT * FROM vehicle_table")
    fun getAllVehicleData(): LiveData<List<VehicleEntity>>

    @Query("DELETE FROM vehicle_table WHERE id = :vehicleId")
    suspend fun deleteVehicleData(vehicleId: Long)

    @Query("SELECT * FROM vehicle_table WHERE id = :vehicleId")
     fun getVehicleData(vehicleId: Long) : VehicleEntity?

    @Query("UPDATE vehicle_table SET isBiFuelVehicle = :isBiFuel WHERE id = :vehicleId")
    suspend fun updateBiFuelState(vehicleId: Long, isBiFuel: Boolean)


    @Update
    suspend fun updateVehicle(vehicle: VehicleEntity)

}
