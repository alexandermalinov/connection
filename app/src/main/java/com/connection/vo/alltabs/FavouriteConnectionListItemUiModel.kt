package com.connection.vo.alltabs

import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.base.isOtherUserOnline
import com.sendbird.android.GroupChannel
import io.getstream.chat.android.client.models.User

data class FavouriteConnectionListItemUiModel(
    val image: String = EMPTY,
    val username: String = EMPTY,
    val online: Boolean = false
)

private fun GroupChannel.toUiModel(loggedUserId: String) = FavouriteConnectionListItemUiModel(
    image = members.find { it.userId != loggedUserId }?.profileUrl ?: EMPTY,
    username = members.find { it.userId != loggedUserId }?.nickname ?: EMPTY,
    online = members.isOtherUserOnline(loggedUserId)
)

fun List<GroupChannel>.toFavouritePeople(loggedUserId: String) = map { it.toUiModel(loggedUserId) }