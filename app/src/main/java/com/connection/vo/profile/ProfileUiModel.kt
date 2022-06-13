package com.connection.vo.profile

import com.connection.data.api.remote.model.user.UserData
import com.connection.utils.common.Constants.EMPTY

data class ProfileUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val username: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val connectionsCount: String = EMPTY,
    val postsCount: String = EMPTY,
    val emptyPosts: Boolean = false
)

fun UserData.toUiModel() = ProfileUiModel(
    id = id,
    profilePicture = picture,
    username = username,
    email = email,
    password = password,
    connectionsCount = connections.keys.size.toString()
)