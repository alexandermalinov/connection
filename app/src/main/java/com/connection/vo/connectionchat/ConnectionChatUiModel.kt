package com.connection.vo.connectionchat

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY

data class ConnectionChatUiModel(
    val header: HeaderUiModel = HeaderUiModel()
) : BaseObservable() {

    @get:Bindable
    var message = EMPTY
        set(value) {
            field = value
            sendClickable = field.isNotBlank()
            notifyPropertyChanged(BR.message)
        }

    @get:Bindable
    var imageMessage: Uri? = Uri.EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.imageMessage)
        }

    @get:Bindable
    var sendClickable = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.sendClickable)
        }

    @get:Bindable
    var connectionStatus = ConnectionStatus.CONNECTED
        set(value) {
            field = value
            handleConnectionStatus(value)
            notifyPropertyChanged(BR.connectionStatus)
        }

    @get:Bindable
    var isRequestTextVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.requestTextVisible)
        }

    @get:Bindable
    var isRequestToConnectTextVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.requestToConnectTextVisible)
        }

    @get:Bindable
    var isRequestButtonVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.requestButtonVisible)
        }

    @get:Bindable
    var isChatBoxVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.chatBoxVisible)
        }

    @get:Bindable
    var isRequestSent = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.requestSent)
        }

    @get:Bindable
    var loadingChatHistory = false
        set(value) {
            field = value
            field = value
            notifyPropertyChanged(BR.loadingChatHistory)
        }

    private fun handleConnectionStatus(status: ConnectionStatus) {
        when (status) {
            ConnectionStatus.CONNECTED -> {
                isChatBoxVisible = true
                isRequestTextVisible = false
                isRequestButtonVisible = false
                isRequestToConnectTextVisible = false
                isRequestSent = false
            }
            ConnectionStatus.INVITE_SENT -> {
                isChatBoxVisible = false
                isRequestTextVisible = false
                isRequestButtonVisible = false
                isRequestToConnectTextVisible = false
                isRequestSent = true
            }
            ConnectionStatus.INVITE_RECEIVED -> {
                isChatBoxVisible = false
                isRequestTextVisible = true
                isRequestButtonVisible = true
                isRequestToConnectTextVisible = false
                isRequestSent = false
            }
            ConnectionStatus.NOT_CONNECTED -> {
                isChatBoxVisible = true
                isRequestTextVisible = false
                isRequestButtonVisible = false
                isRequestToConnectTextVisible = true
                isRequestSent = false
            }
            else -> {

            }
        }
    }

    @get:Bindable
    var shouldOpenGallery = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.shouldOpenGallery)
        }
}
