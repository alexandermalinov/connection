package com.connection.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.connection.data.api.model.user.UserData
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.search.BaseSearchViewModel
import com.connection.vo.search.SearchUiModel
import com.connection.vo.search.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseSearchViewModel() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<SearchUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(SearchUiModel())

    /* --------------------------------------------------------------------------------------------
     * Exposed
    ---------------------------------------------------------------------------------------------*/


    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadUsers(searchText: String) {
        userRepository.searchUsers(
            searchText,
            onSuccess = { users -> onReceiveUsers(users) },
            onFailure = { Timber.e("Error occurred while searching for users") }
        )
    }

    private fun onReceiveUsers(users: List<UserData>) {
        _uiLiveData.value = if (users.isNotEmpty())
            SearchUiModel(users.toUiModel(), false)
        else
            SearchUiModel(emptyList(), true)
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onSearchChanged(searchFlow: Flow<CharSequence>) {
        onSearchChanged(searchFlow) { searchText ->
            loadUsers(searchText.toString())
            lastSearchText = searchText.toString()
        }
    }
}