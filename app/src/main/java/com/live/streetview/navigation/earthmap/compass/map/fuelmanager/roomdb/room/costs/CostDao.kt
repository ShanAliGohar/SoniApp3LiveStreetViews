package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.costs

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling.MileageLogEntity

@Dao
interface CostDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCost(costEntity: CostEntity)

    @Query("SELECT * FROM costs")
    fun getAllCosts(): LiveData<List<CostEntity>>

    @Delete
    suspend fun deleteCost(costEntity: CostEntity)

    @Update
    suspend fun updateCost(costEntity: CostEntity)


    @Query("SELECT * FROM costs WHERE strftime('%Y', substr(selectDate, -4) || '-' || CASE WHEN instr(selectDate, 'January') THEN '01' WHEN instr(selectDate, 'February') THEN '02' WHEN instr(selectDate, 'March') THEN '03' WHEN instr(selectDate, 'April') THEN '04' WHEN instr(selectDate, 'May') THEN '05' WHEN instr(selectDate, 'June') THEN '06' WHEN instr(selectDate, 'July') THEN '07' WHEN instr(selectDate, 'August') THEN '08' WHEN instr(selectDate, 'September') THEN '09' WHEN instr(selectDate, 'October') THEN '10' WHEN instr(selectDate, 'November') THEN '11' WHEN instr(selectDate, 'December') THEN '12' ELSE '00' END || '-' || substr(selectDate, 1, 2)) = strftime('%Y', 'now')")
    fun getCurrentYearCosts(): LiveData<List<CostEntity>>

    @Query("SELECT * FROM costs WHERE strftime('%Y', substr(selectDate, -4) || '-' || CASE WHEN instr(selectDate, 'January') THEN '01' WHEN instr(selectDate, 'February') THEN '02' WHEN instr(selectDate, 'March') THEN '03' WHEN instr(selectDate, 'April') THEN '04' WHEN instr(selectDate, 'May') THEN '05' WHEN instr(selectDate, 'June') THEN '06' WHEN instr(selectDate, 'July') THEN '07' WHEN instr(selectDate, 'August') THEN '08' WHEN instr(selectDate, 'September') THEN '09' WHEN instr(selectDate, 'October') THEN '10' WHEN instr(selectDate, 'November') THEN '11' WHEN instr(selectDate, 'December') THEN '12' ELSE '00' END || '-' || substr(selectDate, 1, 2)) = strftime('%Y', 'now', '-1 year')")
    fun getPreviousYearCosts(): LiveData<List<CostEntity>>

    @Query("SELECT * FROM costs WHERE strftime('%Y-%m', substr(selectDate, -4) || '-' || CASE WHEN instr(selectDate, 'January') THEN '01' WHEN instr(selectDate, 'February') THEN '02' WHEN instr(selectDate, 'March') THEN '03' WHEN instr(selectDate, 'April') THEN '04' WHEN instr(selectDate, 'May') THEN '05' WHEN instr(selectDate, 'June') THEN '06' WHEN instr(selectDate, 'July') THEN '07' WHEN instr(selectDate, 'August') THEN '08' WHEN instr(selectDate, 'September') THEN '09' WHEN instr(selectDate, 'October') THEN '10' WHEN instr(selectDate, 'November') THEN '11' WHEN instr(selectDate, 'December') THEN '12' ELSE '00' END || '-' || substr(selectDate, 1, 2)) = strftime('%Y-%m', 'now')")
    fun getCurrentMonthCosts(): LiveData<List<CostEntity>>

    @Query("SELECT * FROM costs WHERE strftime('%Y-%m', substr(selectDate, -4) || '-' || CASE WHEN instr(selectDate, 'January') THEN '01' WHEN instr(selectDate, 'February') THEN '02' WHEN instr(selectDate, 'March') THEN '03' WHEN instr(selectDate, 'April') THEN '04' WHEN instr(selectDate, 'May') THEN '05' WHEN instr(selectDate, 'June') THEN '06' WHEN instr(selectDate, 'July') THEN '07' WHEN instr(selectDate, 'August') THEN '08' WHEN instr(selectDate, 'September') THEN '09' WHEN instr(selectDate, 'October') THEN '10' WHEN instr(selectDate, 'November') THEN '11' WHEN instr(selectDate, 'December') THEN '12' ELSE '00' END || '-' || substr(selectDate, 1, 2)) = strftime('%Y-%m', 'now', '-1 month')")
    fun getPreviousMonthCosts(): LiveData<List<CostEntity>>


}
