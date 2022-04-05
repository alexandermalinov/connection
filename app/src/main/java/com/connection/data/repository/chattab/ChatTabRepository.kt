package com.connection.data.repository.chattab

import com.connection.data.api.model.ChannelExtraData
import com.connection.data.api.model.UserData
import com.sendbird.android.GroupChannel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import javax.inject.Inject

class ChatTabRepository @Inject constructor(
    private val remote: RemoteSource
) {


    interface RemoteSource {

        suspend fun fetchChannels(
            onSuccess: (List<GroupChannel>) -> Unit,
            onFailure: () -> Unit
        )

        fun connectUser(
            user: UserData,
            onSuccess: (com.sendbird.android.User) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun createChannelByIds(
            membersIds: List<String>,
            extraData: ChannelExtraData,
            onSuccess: (Channel) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun sendMessage(
            channelId: String,
            message: Message,
            onSuccess: (Message) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun getChannel(
            channelId: String,
            getChannel: (Channel) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun fetchMembers(
            isMemberPartOf: Boolean,
            onSuccess: (List<User>) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun createChannel(
            members: List<String>,
            extraData: Map<String, String>,
            onSuccess: (Channel) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun acceptInvite(
            channelId: String,
            message: String,
            onSuccess: () -> Unit,
            onFailure: () -> Unit
        )

        suspend fun declineInvite(
            channelId: String,
            onSuccess: (Channel) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun updateUser(user: User)
    }

    suspend fun fetchChannels(
        onSuccess: (List<GroupChannel>) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.fetchChannels(onSuccess, onFailure)
    }

    fun connectUser(
        user: UserData,
        onSuccess: (com.sendbird.android.User) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.connectUser(user, onSuccess, onFailure)
    }

    suspend fun createChannelByIds(
        membersIds: List<String>,
        extraData: ChannelExtraData,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.createChannelByIds(membersIds, extraData, onSuccess, onFailure)
    }

    suspend fun sendMessage(
        channelId: String,
        message: Message,
        onSuccess: (Message) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.sendMessage(channelId, message, onSuccess, onFailure)
    }

    suspend fun getChannel(
        channelId: String,
        getChannel: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.getChannel(channelId, getChannel, onFailure)
    }

    suspend fun fetchMembers(
        isMemberPartOf: Boolean,
        onSuccess: (List<User>) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.fetchMembers(isMemberPartOf, onSuccess, onFailure)
    }

    suspend fun createChannel(
        members: List<String>,
        extraData: Map<String, String>,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.createChannel(members, extraData, onSuccess, onFailure)
    }

    suspend fun acceptInvite(
        channelId: String,
        message: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        remote.acceptInvite(channelId, message, onSuccess, onFailure)
    }

    suspend fun declineInvite(
        channelId: String,
        onSuccess: (Channel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.declineInvite(channelId, onSuccess, onFailure)
    }

    suspend fun updateUser(user: User) {
        remote.updateUser(user)
    }
}