package com.live.streetview.navigation.earthmap.compass.map.fuelmanager.roomdb.room.trip


import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripDao {

    @Insert
    suspend fun insertTripLog(tripLog: TripEntity)

    @Update
    suspend fun updateTripLog(tripLog: TripEntity)

    @Query("SELECT * FROM trip_logs")
     fun getAllTripLogs(): LiveData<List<TripEntity>>

    @Query("DELETE FROM trip_logs  WHERE id = :tripLogId")
    suspend fun deleteTripLog(tripLogId: Long)


    @Query("SELECT * FROM trip_logs WHERE strftime('%Y', substr(replace(startDate, ',', ''), -4) || '-' || CASE WHEN instr(replace(startDate, ',', ''), 'January') THEN '01' WHEN instr(replace(startDate, ',', ''), 'February') THEN '02' WHEN instr(replace(startDate, ',', ''), 'March') THEN '03' WHEN instr(replace(startDate, ',', ''), 'April') THEN '04' WHEN instr(replace(startDate, ',', ''), 'May') THEN '05' WHEN instr(replace(startDate, ',', ''), 'June') THEN '06' WHEN instr(replace(startDate, ',', ''), 'July') THEN '07' WHEN instr(replace(startDate, ',', ''), 'August') THEN '08' WHEN instr(replace(startDate, ',', ''), 'September') THEN '09' WHEN instr(replace(startDate, ',', ''), 'October') THEN '10' WHEN instr(replace(startDate, ',', ''), 'November') THEN '11' WHEN instr(replace(startDate, ',', ''), 'December') THEN '12' ELSE '00' END || '-' || substr(replace(startDate, ',', ''), 1, 2)) = strftime('%Y', 'now')")
    fun getCurrentYearTripLogs(): LiveData<List<TripEntity>>

    @Query("SELECT * FROM trip_logs WHERE strftime('%Y', substr(replace(startDate, ',', ''), -4) || '-' || CASE WHEN instr(replace(startDate, ',', ''), 'January') THEN '01' WHEN instr(replace(startDate, ',', ''), 'February') THEN '02' WHEN instr(replace(startDate, ',', ''), 'March') THEN '03' WHEN instr(replace(startDate, ',', ''), 'April') THEN '04' WHEN instr(replace(startDate, ',', ''), 'May') THEN '05' WHEN instr(replace(startDate, ',', ''), 'June') THEN '06' WHEN instr(replace(startDate, ',', ''), 'July') THEN '07' WHEN instr(replace(startDate, ',', ''), 'August') THEN '08' WHEN instr(replace(startDate, ',', ''), 'September') THEN '09' WHEN instr(replace(startDate, ',', ''), 'October') THEN '10' WHEN instr(replace(startDate, ',', ''), 'November') THEN '11' WHEN instr(replace(startDate, ',', ''), 'December') THEN '12' ELSE '00' END || '-' || substr(replace(startDate, ',', ''), 1, 2)) = strftime('%Y', 'now', '-1 year')")
    fun getPreviousYearTripLogs(): LiveData<List<TripEntity>>

    @Query("SELECT * FROM trip_logs WHERE strftime('%Y-%m', substr(replace(startDate, ',', ''), -4) || '-' || CASE WHEN instr(replace(startDate, ',', ''), 'January') THEN '01' WHEN instr(replace(startDate, ',', ''), 'February') THEN '02' WHEN instr(replace(startDate, ',', ''), 'March') THEN '03' WHEN instr(replace(startDate, ',', ''), 'April') THEN '04' WHEN instr(replace(startDate, ',', ''), 'May') THEN '05' WHEN instr(replace(startDate, ',', ''), 'June') THEN '06' WHEN instr(replace(startDate, ',', ''), 'July') THEN '07' WHEN instr(replace(startDate, ',', ''), 'August') THEN '08' WHEN instr(replace(startDate, ',', ''), 'September') THEN '09' WHEN instr(replace(startDate, ',', ''), 'October') THEN '10' WHEN instr(replace(startDate, ',', ''), 'November') THEN '11' WHEN instr(replace(startDate, ',', ''), 'December') THEN '12' ELSE '00' END || '-' || substr(replace(startDate, ',', ''), 1, 2)) = strftime('%Y-%m', 'now')")
    fun getCurrentMonthTripLogs(): LiveData<List<TripEntity>>

    @Query("SELECT * FROM trip_logs WHERE strftime('%Y-%m', substr(replace(startDate, ',', ''), -4) || '-' || CASE WHEN instr(replace(startDate, ',', ''), 'January') THEN '01' WHEN instr(replace(startDate, ',', ''), 'February') THEN '02' WHEN instr(replace(startDate, ',', ''), 'March') THEN '03' WHEN instr(replace(startDate, ',', ''), 'April') THEN '04' WHEN instr(replace(startDate, ',', ''), 'May') THEN '05' WHEN instr(replace(startDate, ',', ''), 'June') THEN '06' WHEN instr(replace(startDate, ',', ''), 'July') THEN '07' WHEN instr(replace(startDate, ',', ''), 'August') THEN '08' WHEN instr(replace(startDate, ',', ''), 'September') THEN '09' WHEN instr(replace(startDate, ',', ''), 'October') THEN '10' WHEN instr(replace(startDate, ',', ''), 'November') THEN '11' WHEN instr(replace(startDate, ',', ''), 'December') THEN '12' ELSE '00' END || '-' || substr(replace(startDate, ',', ''), 1, 2)) = strftime('%Y-%m', 'now', '-1 month')")
    fun getPreviousMonthTripLogs(): LiveData<List<TripEntity>>


}
