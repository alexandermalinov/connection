package com.connection.data.repository.search

import com.connection.data.api.local.searchhistory.SearchHistory
import com.connection.data.api.local.searchhistory.SearchHistoryDao
import javax.inject.Inject

class SearchLocalSource @Inject constructor(
    private val searchHistoryDao: SearchHistoryDao
) : SearchRepository.LocalSource {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override suspend fun addSearchSuggestion(searchHistory: SearchHistory) {
        searchHistoryDao.insert(searchHistory)
    }

    override suspend fun getSearchHistory(userId: String): List<SearchHistory> =
        searchHistoryDao.getUserSearchHistory(userId)
}