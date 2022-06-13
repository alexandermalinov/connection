package com.connection.data.api.remote.model.post

import com.connection.utils.common.Constants.EMPTY
import java.util.*

data class Like(
    val likeId: String = UUID.randomUUID().toString(),
    val userId: String = EMPTY,
    val userProfilePicture: String = EMPTY
)

fun Like.toMap() = mapOf(
    "like_id" to likeId,
    "user_id" to userId,
    "user_profile_picture" to userProfilePicture
)
