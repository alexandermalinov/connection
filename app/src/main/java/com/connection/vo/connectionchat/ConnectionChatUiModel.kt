package com.connection.vo.connectionchat

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.message.MessageUiModel

data class ConnectionChatUiModel(
    val header: HeaderUiModel = HeaderUiModel(),
    val messages: List<MessageUiModel> = emptyList(),
    var message: String = EMPTY
) : BaseObservable() {

    @get:Bindable
    var sendClickable: Boolean = false
        set(value) {
            field = value
            field = message.isNotBlank()
            notifyPropertyChanged(BR.sendClickable)
        }
}
