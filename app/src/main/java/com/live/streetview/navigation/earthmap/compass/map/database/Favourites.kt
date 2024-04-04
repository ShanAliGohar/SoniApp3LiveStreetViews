package com.live.streetview.navigation.earthmap.compass.map.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity
data class Favourites(
    @PrimaryKey(autoGenerate = true) val id: Long? = 0,
    val place: String,
    val country: String,
    val lat: Double,
    val lng: Double
)
