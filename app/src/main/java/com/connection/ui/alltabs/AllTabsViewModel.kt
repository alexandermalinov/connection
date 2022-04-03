package com.connection.ui.alltabs

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.api.model.toUiModel
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.connectiontab.ConnectionsPresenter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.alltabs.*
import com.connection.vo.connectiontab.ConnectionTabUiModel
import com.connection.vo.connectiontab.toUiModel
import com.connection.vo.connectiontab.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AllTabsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), AllTabsPresenter, ConnectionsPresenter {

    val uiLiveData: LiveData<AllTabsUiModel>
        get() = _uiLiveData

    val favouriteConnectionsUiLiveData: LiveData<FavouriteConnectionUiModel>
        get() = _favouriteConnectionsUiLiveData

    val connectionUiLiveData: LiveData<List<ConnectionTabUiModel>>
        get() = _connectionsUiLiveDate

    private val _uiLiveData = MutableLiveData(AllTabsUiModel())
    private val _favouriteConnectionsUiLiveData = MutableLiveData(FavouriteConnectionUiModel())
    private val _connectionsUiLiveDate = MutableLiveData(emptyList<ConnectionTabUiModel>())

    private val loggedUserId = savedStateHandle.get<String>(USER_ID) ?: EMPTY
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            initData()
        }
    }

    private suspend fun initData() {
        userRepository.getUser(loggedUserId) { user ->
            viewModelScope.launch {
                loggedUser = user
                initUserData()
                fetchFavouriteConnections()
                fetchChannels()
            }
        }
    }

    private fun initUserData() {
        loggedUser?.toUiModel()?.let {
            chatTabRepository.connectUser(
                it, { user ->
                    loggedUser
                    _uiLiveData.value = AllTabsUiModel(loggedUser?.picture ?: EMPTY)
                }, {
                    Timber.e("error occurred while trying to connect user")
                }
            )
        }
    }

    private suspend fun fetchFavouriteConnections() {
        _uiLiveData.value?.loadingFavourites = true
        chatTabRepository.fetchMembers(false, { users ->
            _favouriteConnectionsUiLiveData.value = FavouriteConnectionUiModel(
                users.map { it.toUiModel() }
            )
            _uiLiveData.value?.loadingFavourites = false
        }, {
            Timber.e("error occurred while settings peoples data")
            _uiLiveData.value?.loadingFavourites = false
        })
    }

    private suspend fun fetchChannels() {
        _uiLiveData.value?.loadingConnections = true
        chatTabRepository.fetchChannels({ channels ->
            _connectionsUiLiveDate.value = channels.toUiModels(loggedUserId)
            _uiLiveData.value?.loadingConnections = false
        }, {
            Timber.e("Failed to fetch channels")
            _uiLiveData.value?.loadingConnections = false
        })
    }

    private fun getConnectionById(id: String) = _connectionsUiLiveDate.value?.first { it.id == id }

    override fun onSearchClick() {
        // TODO ("Not yet implemented")
    }

    override fun onConnectionClick(id: String) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_allMessagesFragment_to_connectionChatFragment,
            bundleOf(
                HEADER_MODEL to getConnectionById(id)?.toUiModel()
            )
        )
    }
}