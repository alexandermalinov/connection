package com.connection.data.api.model

import com.connection.utils.common.Constants.EMPTY
import com.sendbird.android.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val id: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val username: String = EMPTY,
    @SerialName("profile_picture")
    val profilePicture: String = EMPTY,
    val description: String = EMPTY
)

fun User.toUiModel() = User(
    id = userId,
    username = nickname,
    profilePicture = profileUrl
)