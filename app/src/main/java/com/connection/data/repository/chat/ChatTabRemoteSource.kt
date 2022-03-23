package com.connection.data.repository.chat

import com.connection.data.api.model.ChannelExtraData
import com.connection.data.api.model.toExtraData
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.CHANNEL_TYPE_MESSAGING
import com.google.firebase.auth.FirebaseAuth
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.api.models.QueryChannelRequest
import io.getstream.chat.android.client.api.models.QueryChannelsRequest
import io.getstream.chat.android.client.api.models.QuerySort
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import timber.log.Timber
import javax.inject.Inject

class ChatTabRemoteSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val client: ChatClient
) : ChatTabRepository.RemoteSource {

    private val request = QueryChannelsRequest(
        filter = Filters.and(
            Filters.eq("type", "messaging"),
            Filters.`in`("members", listOf("${auth.uid}"))
        ),
        offset = 0,
        limit = 10,
        querySort = QuerySort.desc("last_message_at")
    ).apply {
        watch = true
        state = true
    }

    override suspend fun fetchChannels(
        onSuccess: (List<Channel>) -> Unit,
        onFailure: () -> Unit
    ) {
        client
            .queryChannels(request)
            .enqueue { result ->
                if (result.isSuccess) {
                    onSuccess(result.data())
                } else {
                    onFailure()
                }
            }
    }

    override fun connectUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        client
            .connectUser(user = user, token = client.devToken(user.id))
            .enqueue { result ->
                if (result.isSuccess) {
                    onSuccess()
                } else {
                    Timber.e("error occurred while trying to connect user: ${result.error().message}")
                    onFailure()
                }
            }
    }

    override suspend fun createChannelByIds(
        type: String,
        membersIds: List<String>,
        extraData: ChannelExtraData,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        client.createChannel(
            channelType = type,
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
        getChannel: (Channel) -> Unit
    ) {
        client.queryChannel(
            CHANNEL_TYPE_MESSAGING,
            channelId,
            QueryChannelRequest().withState()
        ).enqueue { result ->
            getChannel.invoke(result.data())
        }
    }
}