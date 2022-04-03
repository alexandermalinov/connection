package com.connection.vo.people

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.api.model.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import io.getstream.chat.android.client.models.User

data class PeopleListItemUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false,
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

fun UserData.toUiModel() = PeopleListItemUiModel(
    profilePicture = picture,
    name = username,
    connectionStatus = ConnectionStatus.NOT_CONNECTED
)

fun User.toUiModel() = PeopleListItemUiModel(
    id = id,
    profilePicture = extraData[Constants.USER_EXTRA_DATA_PICTURE].toString(),
    name = extraData[Constants.USER_EXTRA_DATA_USERNAME].toString(),
    online = online
)

fun List<User>.toUiModels() = map { it.toUiModel() }