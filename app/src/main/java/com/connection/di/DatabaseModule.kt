package com.connection.di

import android.app.Application
import androidx.room.Room
import com.connection.data.local.ConnectionDatabase
import com.connection.utils.common.Constants.DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun provideDatabase(application: Application): ConnectionDatabase =
        Room.databaseBuilder(
            application.applicationContext,
            ConnectionDatabase::class.java,
            DATABASE_NAME
        ).build()

    @Singleton
    @Provides
    fun provideSearchHistoryDao(database: ConnectionDatabase) = database.searchHistoryDao()
}