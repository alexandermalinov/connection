package com.connection.vo.people

import com.connection.data.api.model.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_EXTRA_DATA_PICTURE
import com.connection.utils.common.Constants.USER_EXTRA_DATA_USERNAME
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.name

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

fun User.toUiModel() = PeopleListItemUiModel(
    profilePicture = extraData[USER_EXTRA_DATA_PICTURE].toString(),
    name = extraData[USER_EXTRA_DATA_USERNAME].toString(),
    online = online
)

fun List<User>.toUiModels() = map { it.toUiModel() }