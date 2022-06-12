package com.connection.vo.search

import com.connection.data.api.model.user.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY

data class SearchListItemUiModel(
    val userId: String = EMPTY,
    val username: String = EMPTY,
    val userPicture: String = EMPTY,
    val connectionStatus: ConnectionStatus = ConnectionStatus.NOT_CONNECTED
)

fun List<UserData>.toUiModel() = map { user -> user.toUiModel() }

private fun UserData.toUiModel() = SearchListItemUiModel(
    userId = id,
    username = username,
    userPicture = picture
)
