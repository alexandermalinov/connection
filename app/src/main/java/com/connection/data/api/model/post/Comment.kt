package com.connection.data.api.model.post

import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.comments.PostCommentListItemUiModel

data class Comment(
    val postId: String = EMPTY,
    val commentId: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val comment: String = EMPTY,
    val createdAt: Long = 0L
)

fun Comment.toUiModel() = PostCommentListItemUiModel(
    postId = postId,
    commentId = commentId,
    creatorId = creatorId,
    creatorPicture = creatorPicture,
    creatorUsername = creatorUsername,
    comment = comment,
    createdAt = DateTimeFormatter.formatDayMinutes(createdAt)
)

fun List<Comment>.toUiModel() = map { it.toUiModel() }
