package com.connection.vo.people.notconnected

import com.connection.R
import com.connection.data.remote.response.user.UserData
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.TextRes
import com.connection.vo.connectionchat.HeaderUiModel

data class NotConnectedPeopleListItemUiModel(
    val otherUserId: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val connectionsPictures: List<String> = emptyList(),
    val connectionsCountText: TextRes = TextRes(),
    val connectionsCount: Int = 0
)

fun NotConnectedPeopleListItemUiModel.toUiModel() = HeaderUiModel(
    senderId = otherUserId,
    profilePicture = profilePicture,
    username = name
)

fun List<UserData>.toUiModels() = map { user -> user.toUiModel() }

private fun UserData.toUiModel() = NotConnectedPeopleListItemUiModel(
    otherUserId = id,
    profilePicture = picture,
    name = username,
    connectionsPictures = connections.values.take(4).shuffled(),
    connectionsCountText = TextRes(
        connections.size.toString(),
        getConnectionsCountText(connections.size)
    ),
    connectionsCount = connections.size
)

private fun getConnectionsCountText(connectionsCount: Int) = when {
    connectionsCount in 1..4 -> R.string.connection_count
    connectionsCount > 4 -> R.string.connections_count
    else -> R.string.no_connections_count
}

