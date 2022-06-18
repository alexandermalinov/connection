package com.connection.vo.userprofile

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.remote.response.user.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY

data class UserProfileUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val username: String = EMPTY,
    val connectionsCount: String = EMPTY,
    val postsCount: String = EMPTY,
    val emptyPostsState: Boolean = false
) : BaseObservable() {

    @get:Bindable
    var connectionStatus = ConnectionStatus.NOT_CONNECTED
        set(value) {
            field = value
            handleConnectionStatus(value)
            notifyPropertyChanged(BR.connectionStatus)
        }

    @get:Bindable
    var connectVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.connectVisible)
        }

    @get:Bindable
    var messageVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.messageVisible)
        }

    @get:Bindable
    var acceptDeclineVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.acceptDeclineVisible)
        }

    @get:Bindable
    var inviteSendVisible = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.inviteSendVisible)
        }

    private fun handleConnectionStatus(status: ConnectionStatus) {
        when (status) {
            ConnectionStatus.CONNECTED -> {
                connectVisible = false
                messageVisible = true
                acceptDeclineVisible = false
                inviteSendVisible = false
            }
            ConnectionStatus.INVITE_SENT -> {
                connectVisible = false
                messageVisible = false
                acceptDeclineVisible = false
                inviteSendVisible = true
            }
            ConnectionStatus.INVITE_RECEIVED -> {
                connectVisible = false
                messageVisible = true
                acceptDeclineVisible = true
                inviteSendVisible = false
            }
            ConnectionStatus.NOT_CONNECTED -> {
                connectVisible = true
                messageVisible = false
                acceptDeclineVisible = false
                inviteSendVisible = false
            }
        }
    }
}

fun UserData.toUserProfileUiModel(loggedUserId: String) = UserProfileUiModel(
    id = id,
    profilePicture = picture,
    username = username,
    connectionsCount = connections.keys.size.toString()
).also {
    it.connectionStatus = getConnectionStatus(loggedUserId)
}

private fun UserData.getConnectionStatus(loggedUserId: String) = when {
    connections.keys.any { id -> id == loggedUserId } -> ConnectionStatus.CONNECTED
    else -> ConnectionStatus.NOT_CONNECTED
}
