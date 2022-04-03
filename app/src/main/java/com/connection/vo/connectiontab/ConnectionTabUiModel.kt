package com.connection.vo.connectiontab

import com.connection.ui.base.ConnectionStatus
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.CONNECTION_STATUS_CONNECTED
import com.connection.utils.common.Constants.CONNECTION_STATUS_NOT_CONNECTED
import com.connection.utils.common.Constants.CONNECTION_STATUS_PENDING
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.EXTRA_DATA_CHANNEL_NAME
import com.connection.utils.common.Constants.EXTRA_DATA_CHANNEL_PICTURE
import com.connection.utils.common.Constants.EXTRA_DATA_CHANNEL_STATUS
import com.connection.vo.connectionchat.HeaderUiModel
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.image
import io.getstream.chat.android.client.models.name
import java.util.*

data class ConnectionTabUiModel(
    val id: String = EMPTY,
    val profileImage: String = EMPTY,
    val username: String = EMPTY,
    val lastMessage: String = EMPTY,
    val lastMessageAt: String = EMPTY,
    val unreadMessagesCount: Int = 0,
    val isOnline: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.NOT_CONNECTED
)

fun Channel.toUiModel(senderId: String) = ConnectionTabUiModel(
    id = id,
    profileImage = extraData[EXTRA_DATA_CHANNEL_PICTURE].toString(),
    username = extraData[EXTRA_DATA_CHANNEL_NAME].toString(),
    lastMessage = messages.firstOrNull()?.text ?: EMPTY,
    lastMessageAt = DateTimeFormatter.formatWeekDay(lastMessageAt?.time ?: 0L),
    unreadMessagesCount = unreadCount ?: 0,
    isOnline = getUserOnlineStatus(senderId),
    connectionStatus = extraData[EXTRA_DATA_CHANNEL_STATUS].toString().toConnectionStatus()
)

fun List<Channel>.toUiModels(loggedUserId: String) = map {
    it.toUiModel(loggedUserId)
}

fun ConnectionTabUiModel.toUiModel() = HeaderUiModel(
    channelId = id,
    profilePicture = profileImage,
    username = username,
    isOnline = isOnline,
    connectionStatus = ConnectionStatus.CONNECTED
)

private fun String.toConnectionStatus() = when (this) {
    CONNECTION_STATUS_CONNECTED -> ConnectionStatus.CONNECTED
    CONNECTION_STATUS_NOT_CONNECTED -> ConnectionStatus.NOT_CONNECTED
    else -> ConnectionStatus.PENDING
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