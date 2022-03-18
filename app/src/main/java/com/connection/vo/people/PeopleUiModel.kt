package com.connection.vo.people

import com.connection.data.api.model.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY

data class PeopleUiModel(
    val profilePicture: String = EMPTY,
    val peoples: List<PeopleListItemUiModel> = emptyList()
)

fun UserData.toUiModel() = PeopleListItemUiModel(
    profilePicture = picture,
    name = username,
    connectionStatus = ConnectionStatus.NOT_CONNECTED,
    otherPeoplePictures = emptyList()
)