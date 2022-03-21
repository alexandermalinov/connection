package com.connection.vo.message

import androidx.databinding.BaseObservable
import com.connection.utils.common.Constants.EMPTY

sealed class MessageUiModel : BaseObservable()

data class LoggedUserMessageUiModel(
    val senderPicture: String = EMPTY,
    val senderUsername: String = EMPTY,
    val senderOnline: Boolean = false,
    var senderMessage: String = EMPTY
) : MessageUiModel()

data class SenderUserMessageUiModel(
    val senderPicture: String = EMPTY,
    val senderUsername: String = EMPTY,
    val senderOnline: Boolean = false,
    var senderMessage: String = EMPTY
) : MessageUiModel()

