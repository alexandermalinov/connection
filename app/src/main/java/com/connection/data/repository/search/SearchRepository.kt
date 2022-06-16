package com.connection.data.repository.search

import com.connection.data.api.local.searchhistory.SearchHistory
import com.connection.data.api.remote.model.user.UserData
import javax.inject.Inject

class SearchRepository @Inject constructor(
    private val localSource: SearchLocalSource,
    private val remoteSource: SearchRemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
    ---------------------------------------------------------------------------------------------*/
    interface LocalSource {

        suspend fun addSearchSuggestion(searchHistory: SearchHistory)

        suspend fun getSearchHistory(userId: String): List<SearchHistory>

        suspend fun removeSearchSuggestion(userId: String)
    }

    interface RemoteSource {

        suspend fun searchUsers(
            searchText: String,
            onSuccess: (List<UserData>) -> Unit,
            onFailure: () -> Unit
        )
    }

    suspend fun searchUsers(
        searchText: String,
        onSuccess: (List<UserData>) -> Unit,
        onFailure: () -> Unit
    ) {
        remoteSource.searchUsers(searchText, onSuccess, onFailure)
    }

    suspend fun addSearchSuggestion(searchHistory: SearchHistory) {
        localSource.addSearchSuggestion(searchHistory)
    }

    suspend fun removeSearchSuggestion(userId: String) {
        localSource.removeSearchSuggestion(userId)
    }

    suspend fun getSearchHistory(userId: String) = localSource.getSearchHistory(userId)
}