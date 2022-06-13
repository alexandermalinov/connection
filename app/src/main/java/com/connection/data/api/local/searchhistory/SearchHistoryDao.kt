package com.connection.data.api.local.searchhistory

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    @Query("SELECT * FROM search_history WHERE logged_user_id=:loggedUserId")
    suspend fun getUserSearchHistory(loggedUserId: String): List<SearchHistory>
}