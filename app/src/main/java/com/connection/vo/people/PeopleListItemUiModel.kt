package com.connection.vo.people

import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY

data class PeopleListItemUiModel(
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val connectionStatus: ConnectionStatus = ConnectionStatus.NOT_CONNECTED,
    val otherPeoplePictures: List<String> = emptyList()
)
