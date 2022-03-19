package com.connection.vo.connectionchat

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.message.MessageUiModel

data class ConnectionChatUiModel(
    val header: HeaderUiModel = HeaderUiModel(),
    val messages: List<MessageUiModel> = emptyList()
) : BaseObservable() {

    @get:Bindable
    var message: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.message)
        }

    @get:Bindable
    var sendClickable: Boolean = false
        set(value) {
            field = value
            field = message.isNotBlank()
            notifyPropertyChanged(BR.sendClickable)
        }
}
