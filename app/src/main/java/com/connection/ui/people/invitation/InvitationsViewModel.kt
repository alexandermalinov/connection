package com.connection.ui.people.invitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.chatmessage.ChatMessageRepository
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
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository,
    private val chatMessageRepository: ChatMessageRepository
) : PeopleViewModel(userRepository), InvitationsPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<InvitationsUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(InvitationsUiModel())

    init {
        viewModelScope.launch {
            loadInvitations()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadInvitations() {
        _uiLiveData.value?.loading = true
        chatTabRepository.fetchChannels(
            ConnectionStatus.INVITE_RECEIVED,
            onSuccess = { channels ->
                onChannelsObtained(channels)
            },
            onFailure = {
                Timber.e("Failed to fetch channels")
            })
        _uiLiveData.value?.loading = false
    }

    private fun onChannelsObtained(channels: List<GroupChannel>) {
        channels
            .filter { channel -> channel.joinedMemberCount == 1 }
            .toUiModels(loggedUser?.id ?: EMPTY)
            .let {
                if (it.isNotEmpty())
                    _uiLiveData.value = InvitationsUiModel(it)
                else
                    _uiLiveData.value?.emptyState = true
            }
    }

    private fun updateIsAccepted(channelUrl: String) {
        _uiLiveData.value = _uiLiveData.value?.invitations
            ?.filterNot { it.channelUrl == channelUrl }
            ?.let { InvitationsUiModel(it) }
    }

    private fun updateUserConnections(invitation: InvitationListItemUiModel) {
        userRepository.updateUser(
            userId = loggedUser?.id ?: EMPTY,
            connections = loggedUser?.connections
                ?.plus(Pair(invitation.otherUserId, invitation.profilePicture))
                ?: emptyMap()
        )
    }

    private fun acceptInvitation(
        channel: GroupChannel,
        invitation: InvitationListItemUiModel
    ) {
        channel.acceptInvitation {
            updateIsAccepted(channel.url)
            updateUserConnections(invitation)
        }
    }

    private fun declineInvitation(channel: GroupChannel) {
        channel.declineInvitation { error ->
            if (error == null)
                updateIsAccepted(channel.url)
            else
                Timber.e("error occurred declining invite")
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onAcceptClick(invitation: InvitationListItemUiModel) {
        viewModelScope.launch {
            chatMessageRepository.getChannel(
                channelUrl = invitation.channelUrl,
                onSuccess = { channel ->
                    acceptInvitation(channel, invitation)
                },
                onFailure = {
                    Timber.e("error occurred accepting invite")
                }
            )
        }
    }

    override fun onDeclineClick(invitation: InvitationListItemUiModel) {
        viewModelScope.launch {
            chatMessageRepository.getChannel(
                channelUrl = invitation.channelUrl,
                onSuccess = { channel ->
                    declineInvitation(channel)
                },
                onFailure = {
                    Timber.e("error occurred declining invite")
                }
            )
        }
    }
}