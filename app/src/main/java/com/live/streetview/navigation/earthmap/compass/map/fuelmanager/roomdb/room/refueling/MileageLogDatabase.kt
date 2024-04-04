package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostDao
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs.CostEntity

@Database(entities = [MileageLogEntity::class, CostEntity::class] , version = 6)
abstract class MileageLogDatabase : RoomDatabase() {
    abstract fun mileageDao(): MileageLogDao
    abstract fun costDao() : CostDao
    companion object {
        @Volatile
        private var INSTANCE: MileageLogDatabase? = null
        fun getDatabase(context: Context): MileageLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MileageLogDatabase::class.java,
                    "MileageLog"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}