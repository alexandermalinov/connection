package com.connection.vo.image

import com.connection.utils.common.Constants.EMPTY

data class ImageUiModel(
    val url: String = EMPTY,
    val senderImage: String = EMPTY,
    val senderName: String = EMPTY
)
