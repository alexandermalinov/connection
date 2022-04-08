package com.connection.data.repository.chatmessage

import com.sendbird.android.BaseMessage
import com.sendbird.android.GroupChannel
import com.sendbird.android.UserMessage
import javax.inject.Inject

class ChatMessageRepository @Inject constructor(
    private val remote: ChatMessageRepository.RemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
     ---------------------------------------------------------------------------------------------*/
    interface RemoteSource {

        fun createChannel(
            loggedUserId: String,
            onSuccess: (GroupChannel) -> Unit,
            onFailure: () -> Unit
        )

        fun inviteUser(
            channel: GroupChannel,
            otherUserId: String
        )

        fun getChannel(
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
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    fun createChannel(
        loggedUserId: String,
        onSuccess: (GroupChannel) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.createChannel(loggedUserId, onSuccess, onFailure)
    }

    fun inviteUser(
        channel: GroupChannel,
        otherUserId: String
    ) {
        remote.inviteUser(channel, otherUserId)
    }

    fun getChannel(
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
}
