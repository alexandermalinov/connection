package com.connection.data.repository.chattab

import com.connection.data.remote.response.user.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.PAGING_LIMIT
import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelListQuery
import com.sendbird.android.SendBird
import timber.log.Timber
import javax.inject.Inject


class ChatTabRemoteSource @Inject constructor() : ChatTabRepository.RemoteSource {

    private fun listQuery(filter: ConnectionStatus) = GroupChannel.createMyGroupChannelListQuery()
        .apply {
            isIncludeEmpty = true
            isIncludeMetadata = true
            memberStateFilter = getConnectionStatus(filter)
            order = GroupChannelListQuery.Order.LATEST_LAST_MESSAGE
            limit = PAGING_LIMIT
        }

    private fun getConnectionStatus(filter: ConnectionStatus) = when (filter) {
        ConnectionStatus.CONNECTED -> GroupChannelListQuery.MemberStateFilter.ALL
        ConnectionStatus.INVITE_RECEIVED -> GroupChannelListQuery.MemberStateFilter.INVITED
        else -> GroupChannelListQuery.MemberStateFilter.INVITED
    }

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override suspend fun fetchChannels(
        filter: ConnectionStatus,
        onSuccess: (List<GroupChannel>) -> Unit,
        onFailure: () -> Unit
    ) {
        listQuery(filter).next { channels, error ->
            if (channels != null) {
                onSuccess(channels)
            } else {
                Timber.e("error occurred fetching channels: ${error.message}")
                onFailure()
            }
        }
    }

    override fun connectUser(
        user: UserData,
        onFailure: () -> Unit
    ) {
        SendBird.connect(user.id) { connectedUser, error ->
            if (error == null) {
                connectedUser?.let {
                    SendBird.updateCurrentUserInfo(user.username, user.picture) { error ->
                        if (error == null) {
                            SendBird.setChannelInvitationPreference(false) { preferenceError ->
                                if (preferenceError != null)
                                    onFailure()
                            }
                        } else {
                            onFailure()
                        }
                    }
                }
            } else onFailure()
        }
    }
}