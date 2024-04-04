package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "costs")
data class CostEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "car") val car: String,
    @ColumnInfo(name = "service") val service: String,
    @ColumnInfo(name = "reminder") val reminder: Boolean,
    @ColumnInfo(name = "reminderType") val reminderType: String? = null,
    @ColumnInfo(name = "reminderOdometer") val reminderOdometer: Int? = null,
    @ColumnInfo(name = "reminderDate") val reminderDate: String? = null,
    @ColumnInfo(name = "reminderRecurring") val reminderRecurring: Boolean? = null,
    @ColumnInfo(name = "reminderRepeatDistance") val reminderRepeatDistance: Int? = null,
    @ColumnInfo(name = "reminderRepeatMonths") val reminderRepeatMonths: Int? = null,
    @ColumnInfo(name = "reminderDone") val reminderDone: Boolean? = null,
    @ColumnInfo(name = "costTitle") val costTitle: String,
    @ColumnInfo(name = "totalCost") val totalCost: Double,
    @ColumnInfo(name = "selectDate") val selectDate: String,
    @ColumnInfo(name = "selectTime") val selectTime: String,
    @ColumnInfo(name = "costNote") val costNote: String,
    @ColumnInfo(name = "recurrence") val recurrence: String
)
