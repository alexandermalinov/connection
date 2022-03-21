package com.connection.vo.connectiontab

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import io.getstream.chat.android.client.models.Channel
import java.util.*

data class ConnectionTabUiModel(
    val id: String = EMPTY,
    val profileImage: String = EMPTY,
    val username: String = EMPTY,
    val lastMessage: String = EMPTY,
    val lastMessageAt: String = EMPTY,
    val unreadMessagesCount: Int = 0,
    val isOnline: Boolean = false
)

fun Channel.toUiModel(loggedUserId: String) = ConnectionTabUiModel(
    id = id,
    profileImage = extraData["channel_picture"].toString(),
    username = extraData["channel_name"].toString(),
    lastMessage = messages.lastOrNull()?.text ?: EMPTY,
    lastMessageAt = lastMessageAt?.getLastMessageAt() ?: EMPTY,
    unreadMessagesCount = unreadCount ?: 0,
    isOnline = getUserOnlineStatus(loggedUserId)
)

private fun Date.getLastMessageAt() = if (toString().isBlank().not())
    toString()
else
    EMPTY

private fun Int.getUnreadCount() = if (toString().isBlank().not())
    toString()
else
    EMPTY

fun List<Channel>.toUiModels(loggedUserId: String) = map {
    it.toUiModel(loggedUserId)
}

fun ConnectionTabUiModel.toUiModel() = HeaderUiModel(
    channelId = id,
    profilePicture = profileImage,
    username = username,
    isOnline = isOnline
)

private fun Channel.getUserOnlineStatus(loggedUserId: String) = members
    .firstOrNull { it.user.id == loggedUserId }
    ?.user
    ?.online
    ?: false