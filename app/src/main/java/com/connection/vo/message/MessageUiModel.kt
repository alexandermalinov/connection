package com.connection.vo.message

import androidx.databinding.BaseObservable
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.EXTRA_DATA_PICTURE
import io.getstream.chat.android.client.models.Message

open class MessageUiModel(
    var senderPicture: String = EMPTY,
    var senderMessage: String = EMPTY
) : BaseObservable()

class LoggedUserMessageUiModel : MessageUiModel()

class SenderUserMessageUiModel : MessageUiModel()

fun Message.toUiModel(loggedUserId: String) = if (user.id == loggedUserId)
    LoggedUserMessageUiModel().apply {
        senderPicture = user.extraData[EXTRA_DATA_PICTURE].toString()
        senderMessage = text
    }
else
    SenderUserMessageUiModel().apply {
        senderPicture = user.extraData[EXTRA_DATA_PICTURE].toString()
        senderMessage = text
    }

fun List<Message>.toUiModel(loggedUserId: String) = map {
    it.toUiModel(loggedUserId)
}

