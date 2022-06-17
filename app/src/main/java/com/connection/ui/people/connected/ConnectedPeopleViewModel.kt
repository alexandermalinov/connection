package com.connection.ui.people.connected

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.people.base.PeopleViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.vo.people.connected.ConnectedPeopleListItemUiModel
import com.connection.vo.people.connected.ConnectedUiModel
import com.connection.vo.people.connected.toUiModel
import com.connection.vo.people.connected.toUiModels
import com.sendbird.android.GroupChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectedPeopleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository
) : PeopleViewModel(userRepository), ConnectedPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ConnectedUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(ConnectedUiModel())

    init {
        viewModelScope.launch {
            loadPeople()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadPeople() {
        _uiLiveData.value?.loading = true
        chatTabRepository.fetchChannels(
            ConnectionStatus.CONNECTED,
            onSuccess = { channels ->
                onInvitationsObtained(channels)
            },
            onFailure = {
                Timber.e("Failed to fetch channels")
            })
        _uiLiveData.value?.loading = false
    }

    private fun onInvitationsObtained(channels: List<GroupChannel>) {
        channels
            .toUiModels(loggedUser?.id ?: EMPTY)
            .let {
                if (it.isNotEmpty())
                    _uiLiveData.value = ConnectedUiModel(it)
                else
                    _uiLiveData.value?.emptyState = true
            }
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onChatClick(user: ConnectedPeopleListItemUiModel) {
        _navigationLiveData.value = NavigationGraph(
            R.id.connection_chat_fragment,
            bundleOf(HEADER_MODEL to user.toUiModel())
        )
    }
}