package com.live.streetview.navigation.earthmap.compass.map.di

import android.content.Context
import androidx.room.Room
import com.live.streetview.navigation.earthmap.compass.map.database.AppDatabase
import com.live.streetview.navigation.earthmap.compass.map.database.DataReposiory
import com.live.streetview.navigation.earthmap.compass.map.database.UserCallDao

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "postDatabase").allowMainThreadQueries()
            .build()
    @Provides
    fun providesPostDao(postDatabase: AppDatabase): UserCallDao =
        postDatabase.userDao()

    @Provides
    fun providesPostRepository(postDao: UserCallDao): DataReposiory =
        DataReposiory(postDao)
}