package com.connection.data.api.model.post

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.post.PostUiModel

data class Posts(val posts: List<Post> = emptyList())

data class Post(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val title: String = EMPTY,
    val description: String = EMPTY,
    val picture: String = EMPTY,
    val createAt: Long = 0L,
    val comments: List<Comment> = emptyList()
)

fun Post.toUiModel() = PostUiModel(
    id = id,
    creatorId = creatorId,
    creatorUsername = creatorUsername,
    creatorPicture = creatorPicture,
    picture = picture,
    title = title,
    description = description,
    createAt = createAt,
    comments = comments
)

fun Posts.toUiModels() = posts.map { posts -> posts.toUiModel() }
