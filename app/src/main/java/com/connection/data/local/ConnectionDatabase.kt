package com.connection.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.connection.data.local.searchhistory.SearchHistory
import com.connection.data.local.searchhistory.SearchHistoryDao

@Database(
    entities = [SearchHistory::class],
    version = 1,
    exportSchema = true
)
abstract class ConnectionDatabase : RoomDatabase() {

    abstract fun searchHistoryDao(): SearchHistoryDao
}