package com.connection.data.api.model

import com.connection.utils.common.Constants.EMPTY
import com.sendbird.android.User

data class User(
    val id: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val username: String = EMPTY,
    val picture: String = EMPTY,
    val description: String = EMPTY
)

fun User.toUiModel() = User(
    id = userId,
    username = nickname,
    picture = profileUrl
)