package com.connection.vo.people.connected

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.base.getSender
import com.connection.vo.base.isOtherUserOnline
import com.connection.vo.connectionchat.HeaderUiModel
import com.connection.vo.connectiontab.ConnectionTabUiModel
import com.sendbird.android.GroupChannel

data class ConnectedPeopleListItemUiModel(
    val channelUrl: String = EMPTY,
    val senderId: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false
)

fun GroupChannel.toUiModel(loggedUserId: String) = ConnectedPeopleListItemUiModel(
    senderId = getSender(loggedUserId)?.userId ?: EMPTY,
    channelUrl = url,
    profilePicture = getSender(loggedUserId)?.profileUrl ?: EMPTY,
    name = getSender(loggedUserId)?.nickname ?: EMPTY,
    online = members.isOtherUserOnline(loggedUserId)
)

fun List<GroupChannel>.toUiModels(loggedUserId: String) = map { channel ->
    channel.toUiModel(loggedUserId)
}

fun ConnectedPeopleListItemUiModel.toUiModel() = HeaderUiModel(
    senderId = senderId,
    channelUrl = channelUrl,
    profilePicture = profilePicture,
    username = name,
    isOnline = online,
)
