package com.connection.data.repository.chatmessage

import com.sendbird.android.*
import timber.log.Timber

class ChatMessageRemoteSource : ChatMessageRepository.RemoteSource {

    /* --------------------------------------------------------------------------------------------
     * Private
     ---------------------------------------------------------------------------------------------*/
    private fun createMessagesQuery(unreadMessageCount: Int) = MessageListParams().apply {
        previousResultSize = 100
        nextResultSize = unreadMessageCount
        isInclusive = true
        setIncludeReactions(true)
        setReverse(true)
    }

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override fun createChannel(
        loggedUserId: String,
        onSuccess: (GroupChannel) -> Unit,
        onFailure: () -> Unit
    ) {
        GroupChannel.createChannelWithUserIds(
            listOf(loggedUserId),
            true
        ) { channel, error ->
            if (error == null) {
                onSuccess(channel)
            } else {
                onFailure()
                Timber.e("${error.message}")
            }
        }
    }

    override fun inviteUser(
        channel: GroupChannel,
        otherUserId: String
    ) {
        channel.inviteWithUserIds(listOf(otherUserId)) { error ->
            if (error != null)
                Timber.e("${error.message}")
        }
    }

    override fun getChannel(
        channelUrl: String,
        onSuccess: (GroupChannel) -> Unit,
        onFailure: () -> Unit
    ) {
        GroupChannel.getChannel(channelUrl) { channel, error ->
            if (error == null)
                onSuccess(channel)
            else
                onFailure()
        }
    }

    override fun getChannelMessages(
        channel: GroupChannel,
        onSuccess: (List<BaseMessage>) -> Unit,
        onFailure: () -> Unit
    ) {
        channel.getMessagesByTimestamp(
            channel.lastMessage.createdAt,
            createMessagesQuery(channel.unreadMessageCount)
        ) { messages, error ->
            channel.lastMessage
                ?.let {
                    if (error == null)
                        onSuccess(messages)
                    else
                        onFailure()
                }
                ?: onSuccess(emptyList())
        }
    }

    override fun sendMessage(
        channel: GroupChannel,
        message: String,
        onSuccess: (UserMessage) -> Unit,
        onFailure: () -> Unit
    ) {
        channel.sendUserMessage(message) { userMessage, error ->
            if (error == null)
                onSuccess(userMessage)
            else
                onFailure()
        }
    }
}