package com.connection.data.repository.chatmessage

import com.sendbird.android.BaseMessage
import com.sendbird.android.FileMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.UserMessage
import java.io.File
import javax.inject.Inject

class ChatMessageRepository @Inject constructor(
    private val remote: ChatMessageRepository.RemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
     ---------------------------------------------------------------------------------------------*/
    interface RemoteSource {

        suspend fun createChannel(
            usersIds: Pair<String, String>,
            onSuccess: (GroupChannel) -> Unit,
            onFailure: () -> Unit
        )

        fun inviteUser(
            channel: GroupChannel,
            otherUserId: String,
            onSuccess: () -> Unit,
            onFailure: () -> Unit
        )

        suspend fun getChannel(
            channelUrl: String,
            onSuccess: (GroupChannel) -> Unit,
            onFailure: () -> Unit
        )

        fun getChannelMessages(
            channel: GroupChannel,
            onSuccess: (List<BaseMessage>) -> Unit,
            onFailure: () -> Unit
        )

        fun sendMessage(
            channel: GroupChannel,
            message: String,
            onSuccess: (UserMessage) -> Unit,
            onFailure: () -> Unit
        )

        fun sendImageMessage(
            channel: GroupChannel,
            imageMessage: File,
            onSuccess: (FileMessage) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun acceptInvite(
            channel: GroupChannel,
            onSuccess: () -> Unit,
            onFailure: () -> Unit
        )

        suspend fun declineInvite(
            channel: GroupChannel,
            onSuccess: () -> Unit,
            onFailure: () -> Unit
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    suspend fun createChannel(
        usersIds: Pair<String, String>,
        onSuccess: (GroupChannel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.createChannel(usersIds, onSuccess, onFailure)
    }

    fun inviteUser(
        channel: GroupChannel,
        otherUserId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        remote.inviteUser(channel, otherUserId, onSuccess, onFailure)
    }

    suspend fun getChannel(
        channelUrl: String,
        onSuccess: (GroupChannel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.getChannel(channelUrl, onSuccess, onFailure)
    }

    fun getChannelMessages(
        channel: GroupChannel,
        onSuccess: (List<BaseMessage>) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.getChannelMessages(channel, onSuccess, onFailure)
    }

    fun sendMessage(
        channel: GroupChannel,
        message: String,
        onSuccess: (UserMessage) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.sendMessage(channel, message, onSuccess, onFailure)
    }

    fun sendImageMessage(
        channel: GroupChannel,
        imageMessage: File,
        onSuccess: (FileMessage) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.sendImageMessage(channel, imageMessage, onSuccess, onFailure)
    }

    suspend fun acceptInvite(
        channel: GroupChannel,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        remote.acceptInvite(channel, onSuccess, onFailure)
    }

    suspend fun declineInvite(
        channel: GroupChannel,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        remote.declineInvite(channel, onSuccess, onFailure)
    }
}
