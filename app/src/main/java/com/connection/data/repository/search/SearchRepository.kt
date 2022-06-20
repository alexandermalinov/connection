package com.connection.data.repository.search

import com.connection.data.local.searchhistory.SearchHistory
import com.connection.data.remote.response.user.UserData
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
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
            block: (Either<HttpError, List<UserData>>) -> Unit
        )
    }

    suspend fun searchUsers(
        searchText: String,
        block: (Either<HttpError, List<UserData>>) -> Unit
    ) = remoteSource.searchUsers(searchText, block)

    suspend fun addSearchSuggestion(searchHistory: SearchHistory) {
        localSource.addSearchSuggestion(searchHistory)
    }

    suspend fun removeSearchSuggestion(userId: String) {
        localSource.removeSearchSuggestion(userId)
    }

    suspend fun getSearchHistory(userId: String) = localSource.getSearchHistory(userId)
}