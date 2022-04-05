package com.connection.vo.people

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.api.model.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel

data class PeopleListItemUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false,
    val isConnection: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.NOT_CONNECTED
) : BaseObservable() {

    @get:Bindable
    var loading = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }
}

fun PeopleListItemUiModel.toUiModel(channelId: String) = HeaderUiModel(
    senderId = id,
    channelId = channelId,
    profilePicture = profilePicture,
    username = name,
    isOnline = online,
    connectionStatus = ConnectionStatus.NOT_CONNECTED
)

fun UserData.toUiModel(isConnection: Boolean) = PeopleListItemUiModel(
    id = id,
    profilePicture = picture,
    name = username,
    isConnection = isConnection,
    connectionStatus = ConnectionStatus.NOT_CONNECTED
)

fun List<UserData>.toUiModels(isConnection: Boolean) = map { it.toUiModel(isConnection) }