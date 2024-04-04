package com.live.streetview.navigation.earthmap.compass.map.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserCallDao {
    @Query("SELECT * FROM favourites")
    fun getAll(): Flow<List<Favourites>>

    @Query("SELECT  * FROM ChatRecord group by chat ")
    fun getAllChatRecord(): Flow<List<ChatRecord>>

    @Query("SELECT  * FROM ChatRecord where chat IN (:id) ")
    fun getAllChat(id: Int): List<ChatRecord>

    @Query("SELECT * FROM NewChat ORDER BY id  DESC LIMIT 1")
    fun getLastRecord(): NewChat

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg users: Favourites)

    @Insert
    suspend fun insertNewChat(vararg users: NewChat)

    @Insert
    suspend fun insertChatHistory(vararg users: ChatRecord)

    @Delete
    fun delete(user: Favourites)

     @Query("DELETE FROM ChatRecord WHERE chat = :dhatID")
     fun deleteByUserUrl(dhatID: Int)

}