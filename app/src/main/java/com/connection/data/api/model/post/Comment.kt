package com.connection.data.api.model.post

import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY

data class Comment(
    val id: String = EMPTY,
    val creatorId: String,
    val creatorName: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val text: String = EMPTY,
    val createAt: String = DateTimeFormatter.formatDayOfYear(System.currentTimeMillis())
)
