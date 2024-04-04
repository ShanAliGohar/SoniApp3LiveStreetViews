package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip

// TripLogEntity.kt
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "trip_logs")
data class TripEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val startPoint: String,
    val startOdoCounter: String? = null,
    val startDate: String,
    val startTime: String,
    val endPoint: String,
    val finalOdoCounter: String,
    val endDate: String,
    val endTime: String,
    val costPerKm: Long? = 0L,
    val tripCost: Long? = 0L,
    val description: String,
    val vehicleId : Long,

)
