package com.live.streetview.navigation.earthmap.compass.map.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NewChat(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val newChat: String
)