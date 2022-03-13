package com.connection.data.api.model

import com.connection.utils.common.Constants.EMPTY
import io.getstream.chat.android.client.models.User

data class UsersData(val users: List<UserData> = emptyList())

data class UserData(
    val id: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val username: String = EMPTY,
    val picture: String = EMPTY,
    val description: String = EMPTY
)

fun UserData.toUiModel() = User(
    id = id,
    online = true,
    extraData = mutableMapOf(
        "username" to username,
        "picture" to picture,
        "description" to description
    )
)