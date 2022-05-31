package com.connection.ui.alltabs

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.connectiontab.ConnectionsPresenter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.alltabs.AllTabsUiModel
import com.connection.vo.alltabs.FavouriteConnectionUiModel
import com.connection.vo.alltabs.toFavouritePeople
import com.connection.vo.connectiontab.ConnectionTabUiModel
import com.connection.vo.connectiontab.toUiModel
import com.connection.vo.connectiontab.toUiModels
import com.sendbird.android.GroupChannel
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

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<AllTabsUiModel>
        get() = _uiLiveData

    val connectionUiLiveData: LiveData<List<ConnectionTabUiModel>>
        get() = _connectionsUiLiveDate

    private val _uiLiveData = MutableLiveData(AllTabsUiModel())
    private val _connectionsUiLiveDate = MutableLiveData(emptyList<ConnectionTabUiModel>())
    private val loggedUserId
        get() = loggedUser?.id ?: EMPTY
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            initData()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun initData() {
        userRepository.getLoggedUser { user ->
            viewModelScope.launch {
                loggedUser = user
                _uiLiveData.value = AllTabsUiModel(user?.picture ?: EMPTY)
                fetchChannels()
            }
        }
    }

    private suspend fun fetchChannels() {
        _uiLiveData.value?.loadingConnections = true
        chatTabRepository.fetchChannels(
            ConnectionStatus.CONNECTED,
            onSuccess = { channels ->
                setupChannelsUiData(channels)
            },
            onFailure = {
                Timber.e("Failed to fetch channels")
            })
        _uiLiveData.value?.loadingConnections = false
    }

    private fun setupChannelsUiData(channels: List<GroupChannel>) {
        if (channels.isEmpty()) {
            _uiLiveData.value?.emptyChannelList = true
        } else {
            _connectionsUiLiveDate.value = channels.toUiModels(loggedUserId)
        }
    }

    private fun getConnectionById(id: String) = _connectionsUiLiveDate.value?.first { it.id == id }

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
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