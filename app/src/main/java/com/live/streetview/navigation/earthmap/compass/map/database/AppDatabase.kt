package com.live.streetview.navigation.earthmap.compass.map.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [Favourites::class, NewChat::class, ChatRecord::class],
    version = 1,
    exportSchema = false
)

abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserCallDao


}