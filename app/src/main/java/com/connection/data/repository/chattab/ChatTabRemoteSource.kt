package com.connection.data.repository.chattab

import com.connection.data.api.model.ChannelExtraData
import com.connection.data.api.model.UserData
import com.connection.data.api.model.toExtraData
import com.connection.utils.common.Constants.CHANNEL_TYPE_MESSAGING
import com.connection.utils.common.Constants.PAGING_LIMIT
import com.connection.utils.common.Constants.USER_EXTRA_DATA_PICTURE
import com.connection.utils.common.Constants.USER_EXTRA_DATA_USERNAME
import com.google.firebase.auth.FirebaseAuth
import com.sendbird.android.GroupChannel
import com.sendbird.android.GroupChannelListQuery
import com.sendbird.android.SendBird
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.api.models.QueryUsersRequest
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import timber.log.Timber
import javax.inject.Inject


class ChatTabRemoteSource @Inject constructor(
    auth: FirebaseAuth,
    private val client: ChatClient
) : ChatTabRepository.RemoteSource {

    /* --------------------------------------------------------------------------------------------
     * Properties
     ---------------------------------------------------------------------------------------------*/
    private val connectedUsersQuery = QueryUsersRequest(
        filter = Filters.`in`("members", listOf("${auth.uid}")),
        offset = 0,
        limit = 15,
    )

    private val notConnectedUsersQuery = QueryUsersRequest(
        filter = Filters.`nin`("members", listOf("${auth.uid}")),
        offset = 0,
        limit = 15,
    )

    private val listQuery = GroupChannel.createMyGroupChannelListQuery().apply {
        isIncludeEmpty = true
        isIncludeMetadata = true
        memberStateFilter = GroupChannelListQuery.MemberStateFilter.ALL
        order = GroupChannelListQuery.Order.LATEST_LAST_MESSAGE
        limit = PAGING_LIMIT
    }

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override suspend fun fetchChannels(
        onSuccess: (List<GroupChannel>) -> Unit,
        onFailure: () -> Unit
    ) {
        listQuery.next { channels, error ->
            if (channels != null) {
                onSuccess(channels)
            } else {
                Timber.e("error occurred fetching channels: ${error.message}")
                onFailure()
            }
        }
    }

    override suspend fun fetchMembers(
        isMemberPartOf: Boolean,
        onSuccess: (List<User>) -> Unit,
        onFailure: () -> Unit
    ) {
        client
            .queryUsers(if (isMemberPartOf) connectedUsersQuery else notConnectedUsersQuery)
            .enqueue() {
                if (it.isSuccess)
                    onSuccess(it.data())
                else
                    onFailure()
            }
    }

    override fun connectUser(
        user: UserData,
        onSuccess: (com.sendbird.android.User) -> Unit,
        onFailure: () -> Unit
    ) {
        SendBird.connect(user.id) { connectedUser, error ->
            if (error == null) {
                connectedUser?.let {
                    SendBird.updateCurrentUserInfo(
                        user.username,
                        user.picture
                    ) { error ->
                        if (error == null)
                            onSuccess(it)
                        else
                            onFailure()
                    }
                }
            } else
                onFailure()
        }
    }

    override suspend fun createChannelByIds(
        membersIds: List<String>,
        extraData: ChannelExtraData,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        client.createChannel(
            channelType = CHANNEL_TYPE_MESSAGING,
            members = membersIds,
            extraData = extraData.toExtraData()
        ).enqueue { result ->
            if (result.isSuccess) {
                onSuccess(result.data())
            } else {
                Timber.e("error occurred while trying to create channel: ${result.error().message}")
                onFailure()
            }
        }
    }

    override suspend fun sendMessage(
        channelId: String,
        message: Message,
        onSuccess: (Message) -> Unit,
        onFailure: () -> Unit
    ) {
        client.sendMessage(
            CHANNEL_TYPE_MESSAGING,
            channelId,
            message
        ).enqueue { result ->
            if (result.isSuccess) {
                onSuccess(result.data())
            } else {
                Timber.e("error occurred while sending message: ${result.error().message}")
                onFailure()
            }
        }
    }

    override suspend fun getChannel(
        channelId: String,
        getChannel: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        client.queryChannel(
            CHANNEL_TYPE_MESSAGING,
            channelId,
            QueryChannelRequest().withState()
        ).enqueue { result ->
            if (result.isSuccess)
                getChannel.invoke(result.data())
            else {
                onFailure()
                Timber.e("Error fetching channel: ${result.isError}")
            }
        }
    }

    override suspend fun createChannel(
        members: List<String>,
        extraData: Map<String, String>,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        client
            .createChannel(CHANNEL_TYPE_MESSAGING, members, extraData)
            .enqueue() { result ->
                if (result.isSuccess) {
                    onSuccess(result.data())
                } else {
                    Timber.e("error occurred creating channel: ${result.error().message}")
                    onFailure()
                }
            }
    }

    override suspend fun acceptInvite(
        channelId: String,
        message: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        client.acceptInvite(
            channelType = CHANNEL_TYPE_MESSAGING,
            channelId = channelId,
            message = message
        ).enqueue { result ->
            if (result.isSuccess) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    override suspend fun declineInvite(
        channelId: String,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        client.rejectInvite(
            channelType = CHANNEL_TYPE_MESSAGING,
            channelId = channelId
        ).enqueue { result ->
            if (result.isSuccess) {
                onSuccess(result.data())
            } else {
                onFailure()
            }
        }
    }

    override suspend fun updateUser(user: User) {
        client.updateUser(user).enqueue()
    }
}