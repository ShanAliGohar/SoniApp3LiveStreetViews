package com.live.streetview.navigation.earthmap.compass.map.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatRecord(
    @PrimaryKey(autoGenerate = true) val id: Long?,
    val message: String,
    val date: String,
    val type: Int,
    val chat: Int
)
