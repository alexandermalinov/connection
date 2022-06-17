package com.connection.data.api.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.connection.data.api.local.searchhistory.SearchHistory
import com.connection.data.api.local.searchhistory.SearchHistoryDao

@Database(
    entities = [SearchHistory::class],
    version = 1,
    exportSchema = true
)
abstract class ConnectionDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao
}