package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.vehicle

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "vehicle_table")
data class VehicleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vehicleImage: String?,
    val vehicleName: String,
    val vehicleDescription: String,
    val vehicleMake: String,
    val vehicleModel: String,
    val vehicleYear: Int,
    val vehicleLicence: String,
    val vehicleVn: String,
    val vehicleInsurance: String,
    val isVehicleActive: Boolean,
    val vehicleDistanceUnit: String,
    val vehicleFuelUnit: String,
    val vehicleConsumption: String,
    val vehicleGasType: String,
    val isBiFuelVehicle: Boolean,
    val gasType: String,
    val gasDistanceType: String,
    val carTankCapacity: String
) : Parcelable