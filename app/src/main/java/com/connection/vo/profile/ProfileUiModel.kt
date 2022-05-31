package com.connection.vo.profile

import com.connection.data.api.model.user.UserData
import com.connection.utils.common.Constants.EMPTY

data class ProfileUiModel(
    val profilePicture: String = EMPTY,
    val username: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY
)

fun UserData.toUiModel() = ProfileUiModel(
    profilePicture = picture,
    username = username,
    email = email,
    password = password
)