package com.connection.vo.profile

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.remote.response.user.UserData
import com.connection.utils.common.Constants.EMPTY

data class ProfileUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val username: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val connectionsCount: String = EMPTY,
    val postsCount: String = EMPTY
) : BaseObservable() {

    @get:Bindable
    var emptyPostsState: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.emptyPostsState)
        }
}

fun UserData.toUiModel() = ProfileUiModel(
    id = id,
    profilePicture = picture,
    username = username,
    email = email,
    password = password,
    connectionsCount = connections.keys.size.toString()
)