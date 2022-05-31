package com.connection.vo.people.connected

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.base.getSender
import com.connection.vo.base.isOtherUserOnline
import com.connection.vo.people.PeopleListItemUiModel
import com.connection.vo.people.invitations.InvitationListItemUiModel
import com.sendbird.android.GroupChannel

data class ConnectedPeopleListItemUiModel(
    val otherUserId: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false,
    val channelUrl: String = EMPTY
)

fun GroupChannel.toUiModel(loggedUserId: String) = ConnectedPeopleListItemUiModel(
    otherUserId = getSender(loggedUserId)?.userId ?: EMPTY,
    channelUrl = url,
    profilePicture = getSender(loggedUserId)?.profileUrl ?: EMPTY,
    name = getSender(loggedUserId)?.nickname ?: EMPTY,
    online = members.isOtherUserOnline(loggedUserId)
)

fun List<GroupChannel>.toUiModels(loggedUserId: String) = map { channel ->
    channel.toUiModel(loggedUserId)
}

