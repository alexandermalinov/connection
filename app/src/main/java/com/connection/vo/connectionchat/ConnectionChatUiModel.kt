package com.connection.vo.connectionchat

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.message.MessageListUiModel

data class ConnectionChatUiModel(
    val header: HeaderUiModel = HeaderUiModel()
) : BaseObservable() {

    @get:Bindable
    var message: String = EMPTY
        set(value) {
            field = value
            sendClickable = field.isNotBlank()
            notifyPropertyChanged(BR.message)
        }

    @get:Bindable
    var sendClickable: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.sendClickable)
        }

    @get:Bindable
    var connectionStatus: ConnectionStatus = ConnectionStatus.NOT_CONNECTED
        set(value) {
            field = value
            handleConnectionStatus(value)
            notifyPropertyChanged(BR.connectionStatus)
        }

    @get:Bindable
    var isRequestTextVisible: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.requestTextVisible)
        }

    @get:Bindable
    var isRequestButtonVisible: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.requestButtonVisible)
        }

    @get:Bindable
    var isChatBoxVisible: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.chatBoxVisible)
        }

    private fun handleConnectionStatus(status: ConnectionStatus) {
        when (status) {
            ConnectionStatus.CONNECTED -> {
                isChatBoxVisible = true
                isRequestTextVisible = false
                isRequestButtonVisible = false
            }
            ConnectionStatus.PENDING -> {
                isChatBoxVisible = false
                isRequestTextVisible = true
                isRequestButtonVisible = false
            }
            ConnectionStatus.NOT_CONNECTED -> {
                isChatBoxVisible = false
                isRequestTextVisible = true
                isRequestButtonVisible = true
            }
        }
    }
}
