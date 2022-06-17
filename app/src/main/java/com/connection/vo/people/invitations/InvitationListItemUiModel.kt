package com.connection.vo.people.invitations

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.base.getSender
import com.connection.vo.base.isOtherUserOnline
import com.sendbird.android.GroupChannel

data class InvitationListItemUiModel(
    val otherUserId: String = EMPTY,
    val channelUrl: String = EMPTY,
    val profilePicture: String = EMPTY,
    val name: String = EMPTY,
    val online: Boolean = false
) : BaseObservable() {

    @get:Bindable
    var isAccepted: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.accepted)
        }
}

fun GroupChannel.toUiModel(loggedUserId: String) = InvitationListItemUiModel(
    otherUserId = getSender(loggedUserId)?.userId ?: EMPTY,
    channelUrl = url,
    profilePicture = getSender(loggedUserId)?.profileUrl ?: EMPTY,
    name = getSender(loggedUserId)?.nickname ?: EMPTY,
    online = members.isOtherUserOnline(loggedUserId)
)

fun List<GroupChannel>.toUiModels(loggedUserId: String) = map { channel ->
    channel.toUiModel(loggedUserId)
}
