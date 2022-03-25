package com.connection.vo.message

import androidx.databinding.BaseObservable
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_EXTRA_DATA_PICTURE
import io.getstream.chat.android.client.models.Message

open class MessageListUiModel(
    var senderPicture: String = EMPTY,
    var senderMessage: String = EMPTY,
    var sendAt: String = EMPTY
) : BaseObservable()

class LoggedUserMessageUiModel : MessageListUiModel()

class SenderUserMessageUiModel : MessageListUiModel()

fun Message.toUiModel(loggedUserId: String) = if (user.id == loggedUserId)
    LoggedUserMessageUiModel().apply {
        senderPicture = user.extraData[USER_EXTRA_DATA_PICTURE].toString()
        senderMessage = text
        sendAt = DateTimeFormatter.formatMessageDateTime(createdAt?.time ?: 0L)
    }
else
    SenderUserMessageUiModel().apply {
        senderPicture = user.extraData[USER_EXTRA_DATA_PICTURE].toString()
        senderMessage = text
        sendAt = DateTimeFormatter.formatMessageDateTime(createdAt?.time ?: 0L)
    }

fun List<Message>.toUiModel(loggedUserId: String) = map {
    it.toUiModel(loggedUserId)
}

