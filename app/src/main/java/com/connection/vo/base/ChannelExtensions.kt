package com.connection.vo.base

import com.sendbird.android.GroupChannel
import com.sendbird.android.Member
import com.sendbird.android.User

fun List<Member>.isOtherUserOnline(loggedUserId: String) =
    firstOrNull { it.userId != loggedUserId }?.connectionStatus == User.ConnectionStatus.ONLINE

fun GroupChannel.getSender(loggedUserId: String) = members.find { member ->
    member.userId != loggedUserId
}