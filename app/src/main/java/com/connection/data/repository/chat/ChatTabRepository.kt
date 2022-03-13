package com.connection.data.repository.chat

import com.connection.data.api.model.ChannelExtraData
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

class ChatTabRepository @Inject constructor(
    private val remote: RemoteSource
) {


    interface RemoteSource {

        suspend fun fetchChannels(
            onSuccess: (List<Channel>) -> Unit,
            onFailure: () -> Unit
        )

        fun connectUser(
            user: User,
            onSuccess: () -> Unit,
            onFailure: () -> Unit
        )

        suspend fun createChannelByIds(
            type: String,
            membersIds: List<String>,
            extraData: ChannelExtraData,
            onSuccess: (Channel) -> Unit,
            onFailure: () -> Unit
        )
    }

    suspend fun fetchChannels(
        onSuccess: (List<Channel>) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.fetchChannels(onSuccess, onFailure)
    }

    fun connectUser(
        user: User,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        remote.connectUser(user, onSuccess, onFailure)
    }

    suspend fun createChannelByIds(
        type: String,
        membersIds: List<String>,
        extraData: ChannelExtraData,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.createChannelByIds(type, membersIds, extraData, onSuccess, onFailure)
    }
}