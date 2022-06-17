package com.connection.vo.connectiontab

import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.CONNECTION_REQUEST
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.base.isOtherUserOnline
import com.connection.vo.connectionchat.HeaderUiModel
import com.sendbird.android.GroupChannel

data class ConnectionTabUiModel(
    val id: String = EMPTY,
    val channelUrl: String = EMPTY,
    val profileImage: String = EMPTY,
    val username: String = EMPTY,
    val lastMessage: String = EMPTY,
    val lastMessageAt: String = EMPTY,
    val unreadMessagesCount: Int = 0,
    val isOnline: Boolean = false,
)

/* -------------------------------------------------------------------------------------------------
 * Private
--------------------------------------------------------------------------------------------------*/
private fun GroupChannel.findOtherUser(loggedUserId: String) =
    members.find { it.userId != loggedUserId }

/* -------------------------------------------------------------------------------------------------
 * Exposed
--------------------------------------------------------------------------------------------------*/
fun GroupChannel.toUiModel(loggedUserId: String) = ConnectionTabUiModel(
    id = findOtherUser(loggedUserId)?.userId ?: EMPTY,
    channelUrl = url,
    profileImage = findOtherUser(loggedUserId)?.profileUrl ?: EMPTY,
    username = findOtherUser(loggedUserId)?.nickname ?: EMPTY,
    lastMessage = lastMessage?.message ?: CONNECTION_REQUEST,
    lastMessageAt = DateTimeFormatter.formatWeekDay(lastMessage?.createdAt ?: 0L),
    unreadMessagesCount = unreadMessageCount,
    isOnline = members.isOtherUserOnline(loggedUserId),
)

fun List<GroupChannel>.toUiModels(loggedUserId: String) = map {
    it.toUiModel(loggedUserId)
}

fun ConnectionTabUiModel.toUiModel() = HeaderUiModel(
    senderId = id,
    channelUrl = channelUrl,
    profilePicture = profileImage,
    username = username,
    isOnline = isOnline,
)