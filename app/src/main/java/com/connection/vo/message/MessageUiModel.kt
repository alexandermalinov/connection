package com.connection.vo.message

import com.connection.utils.common.Constants.EMPTY

data class MessageUiModel(
    val senderPicture: String = EMPTY,
    val senderUsername: String = EMPTY,
    val senderOnline: Boolean = false,
    val senderMessage: String = EMPTY
)