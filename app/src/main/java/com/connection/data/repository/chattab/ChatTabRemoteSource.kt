package com.connection.data.repository.chattab

import com.connection.data.remote.response.user.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.PAGING_LIMIT
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelListQuery
import com.sendbird.android.SendBird
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
        block: (Either<HttpError, List<GroupChannel>>) -> Unit
    ) {
        listQuery(filter).next { channels, error ->
            if (channels != null) {
                block.invoke(Either.right(channels))
            } else {
                block.invoke(Either.left(HttpError(serverMessage = error.message)))
            }
        }
    }

    override suspend fun connectUser(
        user: UserData,
        onFailure: () -> Unit
    ) {
        SendBird.connect(user.id) { connectedUser, error ->
            if (error == null) {
                connectedUser?.let {
                    SendBird.updateCurrentUserInfo(user.username, user.picture) { error ->
                        error?.let {
                            SendBird.setChannelInvitationPreference(false) {
                                it?.let { onFailure() }
                            }
                        } ?: onFailure()
                    }
                }
            } else onFailure()
        }
    }
}