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

    private fun createChannelParams(users: List<String>) = GroupChannelParams().apply {
        setPublic(false)
        setEphemeral(false)
        setDistinct(true)
        setSuper(false)
        addUserIds(users)
    }

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override suspend fun createChannel(
        usersIds: Pair<String, String>,
        onSuccess: (GroupChannel) -> Unit,
        onFailure: () -> Unit
    ) {
        GroupChannel.createChannel(createChannelParams(listOf(usersIds.first))) { channel, error ->
            if (error == null) {
                SendBird.setChannelInvitationPreference(false) {
                    onSuccess(channel)
                }
            } else {
                onFailure()
            }
        }
    }

    override fun inviteUser(
        channel: GroupChannel,
        otherUserId: String,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        channel.inviteWithUserIds(listOf(otherUserId)) { error ->
            if (error == null)
                onSuccess()
            else {
                onFailure()
            }
        }
    }

    override suspend fun getChannel(
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
            channel.lastMessage?.createdAt ?: 0L,
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

    override suspend fun acceptInvite(
        channel: GroupChannel,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        channel.acceptInvitation() { error ->
            if (error == null) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    override suspend fun declineInvite(
        channel: GroupChannel,
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ) {
        channel.declineInvitation() { error ->
            if (error == null) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }
}