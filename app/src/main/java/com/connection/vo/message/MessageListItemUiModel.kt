package com.connection.vo.message

import androidx.databinding.BaseObservable
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.sendbird.android.BaseMessage

open class MessageListUiModel(
    var senderPicture: String = EMPTY,
    var senderMessage: String = EMPTY,
    var sendAt: String = EMPTY,
    var isPictureVisible: Boolean = true
) : BaseObservable()

class LoggedUserMessageUiModel : MessageListUiModel()

class SenderUserMessageUiModel : MessageListUiModel()

fun BaseMessage.toUiModel(loggedUserId: String?) =
    if (sender.userId == loggedUserId)
        LoggedUserMessageUiModel().apply {
            senderPicture = sender?.profileUrl ?: EMPTY
            senderMessage = message ?: EMPTY
            sendAt = DateTimeFormatter.formatMessageDateTime(createdAt)
        }
    else
        SenderUserMessageUiModel().apply {
            senderPicture = sender?.profileUrl ?: EMPTY
            senderMessage = message ?: EMPTY
            sendAt = DateTimeFormatter.formatMessageDateTime(createdAt)
        }

fun List<BaseMessage>.toUiModels(loggedUserId: String?) = map {
    it.toUiModel(loggedUserId)
}