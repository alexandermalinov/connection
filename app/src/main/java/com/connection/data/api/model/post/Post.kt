package com.connection.data.api.model.post

import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY

data class Post(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val title: String = EMPTY,
    val description: String = EMPTY,
    val picture: String = EMPTY,
    val createAt: String = DateTimeFormatter.formatDayOfYear(System.currentTimeMillis()),
    val comments: List<Comment> = emptyList()
)
