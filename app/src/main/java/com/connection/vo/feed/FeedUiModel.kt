package com.connection.vo.feed

import com.connection.data.api.model.post.Posts
import com.connection.utils.common.Constants.EMPTY

data class FeedUiModel(
    val posts: Posts = Posts(emptyList()),
    val userId: String = EMPTY
)
