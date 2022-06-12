package com.connection.vo.comments

import com.connection.data.api.model.post.Comment
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import java.util.*

data class PostCommentListItemUiModel(
    val postId: String = EMPTY,
    val commentId: String = UUID.randomUUID().toString(),
    val creatorId: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val comment: String = EMPTY,
    val createdAt: String = DateTimeFormatter.formatDayMinutes(System.currentTimeMillis())
)

fun PostCommentListItemUiModel.toModel() = Comment(
    postId = postId,
    commentId = commentId,
    creatorId = creatorId,
    creatorUsername = creatorUsername,
    creatorPicture = creatorPicture,
    comment = comment,
    createdAt = System.currentTimeMillis()
)
