package com.connection.vo.connectionchat

import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY
import kotlinx.serialization.Serializable

@Serializable
data class HeaderUiModel(
    val senderId: String = EMPTY,
    val channelUrl: String = EMPTY,
    val profilePicture: String = EMPTY,
    val username: String = EMPTY,
    val isOnline: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.NOT_CONNECTED
) : java.io.Serializable
