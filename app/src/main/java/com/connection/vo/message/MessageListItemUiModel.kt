package com.connection.vo.message

import androidx.databinding.BaseObservable
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.INVALID_ID
import com.sendbird.android.BaseMessage
import com.sendbird.android.FileMessage

open class MessageListUiModel(
    var id: Long = INVALID_ID,
    var senderPicture: String = EMPTY,
    var senderMessage: String = EMPTY,
    var sendAt: String = EMPTY,
    var isPictureVisible: Boolean = true
) : BaseObservable()

class LoggedUserMessageUiModel : MessageListUiModel()

class SenderUserMessageUiModel : MessageListUiModel()

class LoggedUserImageMessageUiModel : MessageListUiModel()

class SenderUserImageMessageUiModel : MessageListUiModel()

data class MessageDateUiModel(val date: String) : MessageListUiModel()

fun BaseMessage.toUiModel(loggedUserId: String?) =
    if (sender.userId == loggedUserId)
        LoggedUserMessageUiModel().apply {
            id = messageId
            senderPicture = sender?.profileUrl ?: EMPTY
            senderMessage = message ?: EMPTY
            sendAt = DateTimeFormatter.formatMessageDateTime(createdAt)
        }
    else
        SenderUserMessageUiModel().apply {
            id = messageId
            senderPicture = sender?.profileUrl ?: EMPTY
            senderMessage = message ?: EMPTY
            sendAt = DateTimeFormatter.formatMessageDateTime(createdAt)
        }

fun FileMessage.toUiModel(loggedUserId: String?) = if (sender.userId == loggedUserId)
    LoggedUserImageMessageUiModel().apply {
        id = messageId
        senderPicture = sender?.profileUrl ?: EMPTY
        senderMessage = url ?: EMPTY
        sendAt = DateTimeFormatter.formatMessageDateTime(createdAt)
    }
else
    SenderUserImageMessageUiModel().apply {
        id = messageId
        senderPicture = sender?.profileUrl ?: EMPTY
        senderMessage = url ?: EMPTY
        sendAt = DateTimeFormatter.formatMessageDateTime(createdAt)
    }

fun List<BaseMessage>.toUiModels(loggedUserId: String?) = groupBy {
    DateTimeFormatter.formatDayOfYear(it.createdAt)
}.let {
    val messages = mutableListOf<MessageListUiModel>()
    it.keys.map { date ->
        it[date]?.forEach { message ->
            if (message is FileMessage) {
                messages.add(message.toUiModel(loggedUserId))
            } else {
                messages.add(message.toUiModel(loggedUserId))
            }
        }
        messages.add(MessageDateUiModel(date))
    }
    messages.toList()
}