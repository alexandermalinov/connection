package com.connection.vo.people

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.api.model.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import com.sendbird.android.GroupChannel

data class PeopleListItemUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false,
    val connectionStatus: ConnectionStatus
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
    channelUrl = channelId,
    profilePicture = profilePicture,
    username = name,
    isOnline = online,
)

fun UserData.toUiModel(connectionStatus: ConnectionStatus) = PeopleListItemUiModel(
    id = id,
    profilePicture = picture,
    name = username,
    connectionStatus = connectionStatus
)

fun List<UserData>.toUiModels(connectionStatus: ConnectionStatus) =
    map { it.toUiModel(connectionStatus) }

fun GroupChannel.toUiModel(
    loggedUserId: String,
    connectionStatus: ConnectionStatus
) = PeopleListItemUiModel(
    id = url,
    profilePicture = getSender(loggedUserId)?.profileUrl ?: EMPTY,
    name = getSender(loggedUserId)?.nickname ?: EMPTY,
    connectionStatus = connectionStatus
)

fun List<GroupChannel>.toUiModels(
    loggedUserId: String,
    connectionStatus: ConnectionStatus
) = map { it.toUiModel(loggedUserId, connectionStatus) }

private fun GroupChannel.getSender(loggedUserId: String) = members
    .find { it.userId != loggedUserId }