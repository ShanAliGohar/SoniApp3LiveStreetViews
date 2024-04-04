package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.refueling

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface MileageLogDao {
    @Insert
    suspend fun insertMileageLogData(mileage: MileageLogEntity)

    @Query("SELECT * FROM MileageLog")
    fun getAllMileageLogData(): LiveData<List<MileageLogEntity>>


    @Query("SELECT * FROM MileageLog")
    fun getAllMileageLogData2():List<MileageLogEntity>

    @Query("DELETE FROM MileageLog WHERE id = :mileageId")
    suspend fun deleteMileageLogData(mileageId: Long)

    @Query("SELECT * FROM MileageLog WHERE strftime('%Y', substr(date, -4) || '-' || CASE WHEN instr(date, 'January') THEN '01' WHEN instr(date, 'February') THEN '02' WHEN instr(date, 'March') THEN '03' WHEN instr(date, 'April') THEN '04' WHEN instr(date, 'May') THEN '05' WHEN instr(date, 'June') THEN '06' WHEN instr(date, 'July') THEN '07' WHEN instr(date, 'August') THEN '08' WHEN instr(date, 'September') THEN '09' WHEN instr(date, 'October') THEN '10' WHEN instr(date, 'November') THEN '11' WHEN instr(date, 'December') THEN '12' ELSE '00' END || '-' || substr(date, 1, 2)) = strftime('%Y', 'now')")
    fun getCurrentYearMileageLogs(): LiveData<List<MileageLogEntity>>

    @Query("SELECT * FROM MileageLog WHERE strftime('%Y', substr(date, -4) || '-' || CASE WHEN instr(date, 'January') THEN '01' WHEN instr(date, 'February') THEN '02' WHEN instr(date, 'March') THEN '03' WHEN instr(date, 'April') THEN '04' WHEN instr(date, 'May') THEN '05' WHEN instr(date, 'June') THEN '06' WHEN instr(date, 'July') THEN '07' WHEN instr(date, 'August') THEN '08' WHEN instr(date, 'September') THEN '09' WHEN instr(date, 'October') THEN '10' WHEN instr(date, 'November') THEN '11' WHEN instr(date, 'December') THEN '12' ELSE '00' END || '-' || substr(date, 1, 2)) = strftime('%Y', 'now', '-1 year')")
    fun getPreviousYearMileageLogs(): LiveData<List<MileageLogEntity>>

    @Query("SELECT * FROM MileageLog WHERE strftime('%Y-%m', substr(date, -4) || '-' || CASE WHEN instr(date, 'January') THEN '01' WHEN instr(date, 'February') THEN '02' WHEN instr(date, 'March') THEN '03' WHEN instr(date, 'April') THEN '04' WHEN instr(date, 'May') THEN '05' WHEN instr(date, 'June') THEN '06' WHEN instr(date, 'July') THEN '07' WHEN instr(date, 'August') THEN '08' WHEN instr(date, 'September') THEN '09' WHEN instr(date, 'October') THEN '10' WHEN instr(date, 'November') THEN '11' WHEN instr(date, 'December') THEN '12' ELSE '00' END || '-' || substr(date, 1, 2)) = strftime('%Y-%m', 'now')")
    fun getCurrentMonthMileageLogs(): LiveData<List<MileageLogEntity>>

    @Query("SELECT * FROM MileageLog WHERE strftime('%Y-%m', substr(date, -4) || '-' || CASE WHEN instr(date, 'January') THEN '01' WHEN instr(date, 'February') THEN '02' WHEN instr(date, 'March') THEN '03' WHEN instr(date, 'April') THEN '04' WHEN instr(date, 'May') THEN '05' WHEN instr(date, 'June') THEN '06' WHEN instr(date, 'July') THEN '07' WHEN instr(date, 'August') THEN '08' WHEN instr(date, 'September') THEN '09' WHEN instr(date, 'October') THEN '10' WHEN instr(date, 'November') THEN '11' WHEN instr(date, 'December') THEN '12' ELSE '00' END || '-' || substr(date, 1, 2)) = strftime('%Y-%m', 'now', '-1 month')")
    fun getPreviousMonthMileageLogs(): LiveData<List<MileageLogEntity>>





}