package com.live.streetview.navigation.earthmap.compass.map.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.live.streetview.navigation.earthmap.compass.map.places.MainPlacesModel
import com.live.streetview.navigation.earthmap.compass.map.database.ChatRecord
import com.live.streetview.navigation.earthmap.compass.map.database.DataReposiory
import com.live.streetview.navigation.earthmap.compass.map.database.Favourites
import com.live.streetview.navigation.earthmap.compass.map.database.NewChat
import com.live.streetview.navigation.earthmap.compass.map.places.RetrofitService
import com.live.streetview.navigation.earthmap.compass.map.weatherinstance.RetrofitInstanceClass
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val postRepository: DataReposiory) : ViewModel() {
    val places = RetrofitInstanceClass.weeklyWeatherRetrofitInstance?.create(
        RetrofitService::class.java
    )
    val data = MutableLiveData<MainPlacesModel>()

    suspend fun getData(query: String): Flow<MainPlacesModel?> {
        return flow {
            emit(places?.getPlaces(query))
        }.flowOn(Dispatchers.IO)
    }

    fun saveDataToDataBase(data: Favourites) {
        CoroutineScope(Dispatchers.IO).launch {
            postRepository.insert(data)
        }
    }

    fun saveDataToDataBaseForNewChat(data: NewChat) {
        CoroutineScope(Dispatchers.IO).launch {
            postRepository.insertNewChat(data)
        }
    }

    fun saveDataToDataBaseChatHistory(data: ChatRecord) {
        CoroutineScope(Dispatchers.IO).launch {
            postRepository.insertChatHistory(data)
        }
    }

    fun getAllFav(): Flow<List<Favourites>> {
        return postRepository.getAllPost
    }

    fun getChatHistory(): Flow<List<ChatRecord>> {
        return postRepository.getChatHistory
    }

    fun getAllChatHistory(id: Int): Flow<List<ChatRecord>> {
        return flow {
            emit(postRepository.getSpecific(id))
        }.flowOn(Dispatchers.IO)

    }

    fun getLastRecord(): Flow<NewChat> {
        return flow {
            emit(postRepository.getlastRecord)
        }.flowOn(Dispatchers.IO)
    }
    fun deleteById(url: Int) {
        return postRepository.deleteById(url)
    }
}


