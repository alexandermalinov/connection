package com.connection.vo.connectiontab

import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import com.sendbird.android.GroupChannel
import com.sendbird.android.Member
import com.sendbird.android.User

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

fun GroupChannel.toUiModel(loggedUserId: String) = ConnectionTabUiModel(
    id = members.find { it.userId != loggedUserId }?.userId ?: EMPTY,
    channelUrl = url,
    profileImage = members.find { it.userId != loggedUserId }?.profileUrl ?: EMPTY,
    username = members.find { it.userId != loggedUserId }?.nickname ?: EMPTY,
    lastMessage = lastMessage?.message ?: "Connection Request",
    lastMessageAt = DateTimeFormatter.formatWeekDay(lastMessage?.createdAt ?: 0L),
    unreadMessagesCount = unreadMessageCount,
    isOnline = members.isOtherUserOnline(loggedUserId),
)

/* -------------------------------------------------------------------------------------------------
 * Exposed
--------------------------------------------------------------------------------------------------*/
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

/* -------------------------------------------------------------------------------------------------
 * Private
--------------------------------------------------------------------------------------------------*/
private fun List<Member>.isOtherUserOnline(loggedUserId: String) =
    firstOrNull { it.userId != loggedUserId }?.connectionStatus == User.ConnectionStatus.ONLINE