package com.live.streetview.navigation.earthmap.compass.map.database

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class DataReposiory @Inject constructor(private val callDao: UserCallDao) {

    val getAllPost = callDao.getAll()
    val getlastRecord = callDao.getLastRecord()
    val getChatHistory = callDao.getAllChatRecord()
//    val getChat = callDao.getAllChat(id)
    suspend fun getSpecific(id: Int): List<ChatRecord> {
        return callDao.getAllChat(id)
    }

    suspend fun insert(postList: Favourites) {
        callDao.insertAll(postList)
    }

    suspend fun insertNewChat(postList: NewChat) {
        callDao.insertNewChat(postList)
    }

    suspend fun insertChatHistory(postList: ChatRecord) {
        callDao.insertChatHistory(postList)
    }

    fun deleteById(url: Int) {
        callDao.deleteByUserUrl(url)
    }
}