package com.connection.data.remote.response.post

import com.connection.data.remote.response.user.UserData
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.post.PostUiModel

data class Posts(val posts: List<Post> = emptyList())

data class Post(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val description: String = EMPTY,
    val picture: String = EMPTY,
    val createAt: Long = 0L,
    val comments: Map<String, Comment> = emptyMap(),
    val likes: Map<String, String> = emptyMap()
)

fun Post.toUiModel(loggedUser: UserData?) = PostUiModel(
    id = id,
    creatorId = creatorId,
    creatorUsername = creatorUsername,
    creatorPicture = creatorPicture,
    picture = picture,
    description = description,
    createdAt = DateTimeFormatter.formatDayMinutes(createAt),
    commentsCount = comments.values.size.toString(),
    likes = likes,
    isConnectVisible = isUserConnection(loggedUser)
).also { post ->
    post.isLiked = likes.keys.any { it == loggedUser?.id }
    post.likesCount = likes.keys.size.toString()
}

fun List<Post>.toUiModels(loggedUser: UserData?) = map { posts -> posts.toUiModel(loggedUser) }

private fun Post.isUserConnection(loggedUser: UserData?) = loggedUser?.connections?.keys
    ?.none { it == creatorId } == true && loggedUser.id != creatorId
