package com.connection.vo.connectiontab

import com.connection.ui.base.ConnectionStatus
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.CONNECTION_STATUS_CONNECTED
import com.connection.utils.common.Constants.CONNECTION_STATUS_NOT_CONNECTED
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import com.sendbird.android.GroupChannel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.image
import io.getstream.chat.android.client.models.name

data class ConnectionTabUiModel(
    val id: String = EMPTY,
    val channelUrl: String = EMPTY,
    val profileImage: String = EMPTY,
    val username: String = EMPTY,
    val lastMessage: String = EMPTY,
    val lastMessageAt: String = EMPTY,
    val unreadMessagesCount: Int = 0,
    val isOnline: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.CONNECTED
)

fun GroupChannel.toUiModel(loggedUserId: String) = ConnectionTabUiModel(
    id = members.find { it.userId != loggedUserId }?.userId ?: EMPTY,
    channelUrl = url,
    profileImage = members.find { it.userId != loggedUserId }?.profileUrl ?: EMPTY,
    username = members.find { it.userId != loggedUserId }?.nickname ?: EMPTY,
    lastMessage = lastMessage?.message ?: "Connection Request",
    lastMessageAt = DateTimeFormatter.formatWeekDay(lastMessage?.createdAt ?: 0L),
    unreadMessagesCount = unreadMessageCount,
    isOnline = members.find { it.userId != loggedUserId }?.isActive ?: false,
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
    connectionStatus = ConnectionStatus.CONNECTED
)

private fun String.toConnectionStatus() = when (this) {
    CONNECTION_STATUS_CONNECTED -> ConnectionStatus.CONNECTED
    CONNECTION_STATUS_NOT_CONNECTED -> ConnectionStatus.NOT_CONNECTED
    else -> ConnectionStatus.REQUEST_SENT
}

private fun Channel.getSenderName(senderId: String) = members
    .firstOrNull { it.user.id == senderId }
    ?.user
    ?.name
    ?: EMPTY

private fun Channel.getSenderImage(senderId: String) = members
    .firstOrNull { it.user.id == senderId }
    ?.user
    ?.image
    ?: EMPTY

fun Channel.getUserOnlineStatus(senderId: String) = members
    .firstOrNull { it.user.id == senderId }
    ?.user
    ?.online
    ?: false