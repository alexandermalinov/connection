package com.connection.vo.alltabs

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.base.isOtherUserOnline
import com.connection.vo.connectionchat.HeaderUiModel
import com.sendbird.android.GroupChannel

data class FavouriteConnectionListItemUiModel(
    val id: String = EMPTY,
    val channelUrl: String = EMPTY,
    val image: String = EMPTY,
    val username: String = EMPTY,
    val online: Boolean = false
)

private fun GroupChannel.toUiModel(loggedUserId: String) = FavouriteConnectionListItemUiModel(
    id = findOtherUser(loggedUserId)?.userId ?: EMPTY,
    channelUrl = url,
    image = findOtherUser(loggedUserId)?.profileUrl ?: EMPTY,
    username = findOtherUser(loggedUserId)?.nickname ?: EMPTY,
    online = members.isOtherUserOnline(loggedUserId)
)

private fun GroupChannel.findOtherUser(loggedUserId: String) =
    members.find { it.userId != loggedUserId }

fun List<GroupChannel>.toFavouriteConnections(loggedUserId: String) = FavouriteConnectionUiModel(
    map { it.toUiModel(loggedUserId) }
)

fun FavouriteConnectionListItemUiModel.toUiModel() = HeaderUiModel(
    senderId = id,
    channelUrl = channelUrl,
    profilePicture = image,
    username = username,
    isOnline = online,
)