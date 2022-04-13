package com.connection.ui.people.invitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.people.base.PeopleViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.people.invitations.InvitationListItemUiModel
import com.connection.vo.people.invitations.InvitationsUiModel
import com.connection.vo.people.invitations.toUiModels
import com.sendbird.android.GroupChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository
) : PeopleViewModel(userRepository), InvitationsPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<InvitationsUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(InvitationsUiModel())

    init {
        viewModelScope.launch {
            setupPeople()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun setupPeople() {
        _uiLiveData.value?.apply {
            loading = true
            chatTabRepository.fetchChannels(
                ConnectionStatus.INVITE_RECEIVED,
                onSuccess = { channels ->
                    onChannelsObtained(channels)
                    loading = false
                },
                onFailure = {
                    Timber.e("Failed to fetch channels")
                    loading = false
                })
        }
    }

    private fun onChannelsObtained(channels: List<GroupChannel>) {
        channels
            .filter { channel -> channel.joinedMemberCount == 1 }
            .toUiModels(loggedUser?.id ?: EMPTY)
            .let {
                if (it.isNullOrEmpty().not())
                    _uiLiveData.value = InvitationsUiModel(it)
                else
                    _uiLiveData.value?.emptyState = true
            }
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onAcceptClick(invitation: InvitationListItemUiModel) {
        // TODO("Not yet implemented")
    }

    override fun onDeclineClick(invitation: InvitationListItemUiModel) {
        // TODO("Not yet implemented")
    }
}