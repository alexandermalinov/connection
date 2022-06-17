package com.connection.vo.feed

import com.connection.utils.common.Constants.EMPTY

data class FeedUiModel(
    val profilePicture: String = EMPTY,
    val emptyChats: Boolean = false,
    val emptyPosts: Boolean = false
)
