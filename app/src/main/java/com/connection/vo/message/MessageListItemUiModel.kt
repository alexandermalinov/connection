package com.connection.vo.message

import androidx.databinding.BaseObservable
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.sendbird.android.BaseMessage
import com.sendbird.android.UserMessage

open class MessageListUiModel(
    var senderPicture: String = EMPTY,
    var senderMessage: String = EMPTY,
    var sendAt: String = EMPTY
) : BaseObservable()

class LoggedUserMessageUiModel : MessageListUiModel()

class SenderUserMessageUiModel : MessageListUiModel()

fun BaseMessage.toUiModel(loggedUserId: String?) =
    if (sender.userId == loggedUserId)
        LoggedUserMessageUiModel().apply {
            senderPicture = sender?.profileUrl ?: EMPTY
            senderMessage = message ?: EMPTY
            sendAt = DateTimeFormatter.formatMessageDateTime(createdAt ?: 0L)
        }
    else
        SenderUserMessageUiModel().apply {
            senderPicture = sender?.profileUrl ?: EMPTY
            senderMessage = message  ?: EMPTY
            sendAt = DateTimeFormatter.formatMessageDateTime(createdAt ?: 0L)
        }

fun List<BaseMessage>.toUiModels(loggedUserId: String?) = map {
    it.toUiModel(loggedUserId)
}