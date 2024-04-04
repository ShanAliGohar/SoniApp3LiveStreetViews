package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "MileageLog")
data class MileageLogEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val odometerType: String,
    val odometerKm : String,
    val carId : Long,
    val fuelLevel : String,
    val gas: Int,
    val gasType: String,
    val price: Int,
    val cost: Long,
    val date: String,
    val time: String,
    val tankLevel: Boolean,
    val tankLevelBefore: String,
    val tankLevelAfter: String,
    val gasStationType: String,
    val discountStatus: Boolean,
    val discountPerLiter: Float,
    val totalDiscount: Float,
    val addNote: String
)