package com.connection.ui.search

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.local.searchhistory.toUiModels
import com.connection.data.remote.response.user.UserData
import com.connection.data.repository.search.SearchRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.search.BaseSearchViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.INVALID_RES
import com.connection.utils.common.Constants.USER_ID
import com.connection.utils.responsehandler.HttpError
import com.connection.vo.search.SearchUiModel
import com.connection.vo.search.toModel
import com.connection.vo.search.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val userRepository: UserRepository
) : BaseSearchViewModel(), UserSearchPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<SearchUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(SearchUiModel())
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            loadLoggedUser()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadLoggedUser() {
        userRepository.getLoggedUser { either ->
            viewModelScope.launch {
                either.foldSuspend({ error ->
                    Timber.e("Error occurred while fetching user data: $error")
                }, { user ->
                    loggedUser = user
                    loadRecentSearches()
                })
            }
        }
    }

    private suspend fun loadUsers(searchText: String) {
        _uiLiveData.value?.loadingState = true
        searchRepository.searchUsers(searchText) { either ->
            either.fold({ error ->
                loadErrorState(error)
            }, { users ->
                onReceiveUsers(users)
                _uiLiveData.value?.loadingState = false
            })
        }
    }

    private suspend fun loadRecentSearches() {
        _uiLiveData.value = _uiLiveData.value?.copy(
            searchList = searchRepository
                .getSearchHistory(loggedUser?.id ?: EMPTY)
                .toUiModels(),
            emptySearchResultState = false,
            recentSearchesSate = true
        )
    }

    private fun loadErrorState(error: HttpError) {
        _uiLiveData.value = SearchUiModel(
            searchList = emptyList(),
            emptySearchResultState = true,
            recentSearchesSate = false,
            errorTextRes = error.errorMessageRes ?: INVALID_RES
        )
    }

    private fun onReceiveUsers(users: List<UserData>) {
        _uiLiveData.value = if (users.isNotEmpty())
            SearchUiModel(
                searchList = users.toUiModel(),
                emptySearchResultState = false,
                recentSearchesSate = false,
                errorTextRes = INVALID_RES
            )
        else
            SearchUiModel(
                searchList = emptyList(),
                emptySearchResultState = true,
                recentSearchesSate = false,
                errorTextRes = R.string.no_connections
            )
    }

    private suspend fun addSearchSuggestion(id: String) {
        _uiLiveData.value?.searchList
            ?.find { it.userId == id }
            ?.let { search ->
                searchRepository.addSearchSuggestion(
                    search.toModel(loggedUser?.id ?: EMPTY)
                )
            }
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onSearchChanged(searchFlow: Flow<CharSequence>) {
        onSearchChanged(searchFlow) { searchText ->
            if (searchText.toString().isBlank()) {
                loadRecentSearches()
            } else {
                loadUsers(searchText.toString())
            }
            lastSearchText = searchText.toString()
        }
    }

    override fun onUserClick(id: String) {
        viewModelScope.launch {
            addSearchSuggestion(id)
            _navigationLiveData.value = NavigationGraph(
                R.id.action_searchFragment_to_userProfileFragment,
                bundleOf(USER_ID to id)
            )
        }
    }

    override fun onRemoveClick(id: String) {
        viewModelScope.launch {
            _uiLiveData.value?.searchList
                ?.find { it.userId == id }
                ?.let {
                    _uiLiveData.value = _uiLiveData.value
                        ?.copy(searchList = _uiLiveData.value?.searchList?.minus(it) ?: emptyList())
                }
            searchRepository.removeSearchSuggestion(id)
        }
    }
}