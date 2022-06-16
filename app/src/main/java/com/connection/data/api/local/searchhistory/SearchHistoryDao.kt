package com.connection.data.api.local.searchhistory

import androidx.room.*

@Dao
interface SearchHistoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistory: SearchHistory)

    @Query("DELETE FROM search_history WHERE searched_user_id = :userId")
    suspend fun delete(userId: String)

    @Query("SELECT * FROM search_history WHERE logged_user_id=:loggedUserId")
    suspend fun getUserSearchHistory(loggedUserId: String): List<SearchHistory>
}