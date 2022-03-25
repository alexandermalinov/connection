package com.connection.ui.alltabs

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.ChannelExtraData
import com.connection.data.api.model.UserData
import com.connection.data.api.model.toUiModel
import com.connection.data.repository.chat.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.connectiontab.ConnectionsPresenter
import com.connection.utils.common.Constants.CHANNEL_TYPE_MESSAGING
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.alltabs.*
import com.connection.vo.connectiontab.toUiModel
import com.connection.vo.connectiontab.toUiModels
import com.connection.vo.people.toUiModels
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

    private val _uiLiveData = MutableLiveData(AllTabsUiModel())
    private val _favouriteConnectionsUiLiveData = MutableLiveData(FavouriteConnectionUiModel())

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
                it, {
                    _uiLiveData.value = AllTabsUiModel(
                        loggedUser?.picture ?: EMPTY,
                        emptyList()
                    )
                }, {
                    Timber.e("error occurred while trying to connect user")
                }
            )
        }
    }

    private suspend fun fetchFavouriteConnections() {
        chatTabRepository.fetchMembers({ users ->
            _favouriteConnectionsUiLiveData.value = FavouriteConnectionUiModel(
                users.map { it.toUiModel() }
            )
        }, {
            Timber.e("error occurred while settings peoples data")
        })
    }

    private suspend fun fetchChannels() {
        chatTabRepository.fetchChannels({ channels ->
            _uiLiveData.value = AllTabsUiModel(
                profileImage = _uiLiveData.value?.profileImage ?: EMPTY,
                connections = channels.toUiModels(loggedUserId)
            )
        }, {
            Timber.e("Failed to fetch channels")
        })
    }

    private fun getConnectionById(id: String) = _uiLiveData.value
        ?.connections
        ?.first { it.id == id }

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