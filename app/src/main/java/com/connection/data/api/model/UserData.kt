package com.connection.data.api.model

import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_EXTRA_DATA_DESCRIPTION
import com.connection.utils.common.Constants.USER_EXTRA_DATA_PICTURE
import com.connection.utils.common.Constants.USER_EXTRA_DATA_USERNAME
import io.getstream.chat.android.client.models.User

data class UsersData(val users: List<UserData> = emptyList())

data class UserData(
    val id: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val username: String = EMPTY,
    val picture: String = EMPTY,
    val description: String = EMPTY
): java.io.Serializable

fun UserData.toUiModel() = User(
    id = id,
    extraData = mutableMapOf(
        USER_EXTRA_DATA_USERNAME to username,
        USER_EXTRA_DATA_PICTURE to picture,
        USER_EXTRA_DATA_DESCRIPTION to description
    )
)