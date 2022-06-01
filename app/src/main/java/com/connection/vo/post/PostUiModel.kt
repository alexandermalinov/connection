package com.connection.vo.post

import android.net.Uri
import com.connection.data.api.model.post.Comment
import com.connection.data.api.model.post.Post
import com.connection.utils.common.Constants.EMPTY
import java.util.*

data class PostUiModel(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val picture: String = EMPTY,
    var title: String = EMPTY,
    var description: String = EMPTY,
    val createAt: Long = 0L,
    val comments: List<Comment> = emptyList()
)

fun PostUiModel.toPost(picture: Uri?) = Post(
    id = UUID.randomUUID().toString(),
    creatorId = creatorId,
    creatorUsername = creatorUsername,
    creatorPicture = creatorPicture,
    title = title,
    description = description,
    picture = picture.toString(),
    comments = comments,
    createAt = System.currentTimeMillis()
)
