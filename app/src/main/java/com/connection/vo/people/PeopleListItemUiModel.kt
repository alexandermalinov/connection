package com.connection.vo.people

import com.connection.data.api.model.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import com.sendbird.android.GroupChannel

open class PeopleListItemUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false
)

fun PeopleListItemUiModel.toUiModel(channelUrl: String) = HeaderUiModel(
    senderId = id,
    channelUrl = channelUrl,
    profilePicture = profilePicture,
    username = name,
    isOnline = online,
)