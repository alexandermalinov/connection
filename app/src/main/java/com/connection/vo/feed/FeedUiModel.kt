package com.connection.vo.feed

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY

data class FeedUiModel(
    val profilePicture: String = EMPTY,
    val emptyChats: Boolean = false,
    val emptyPosts: Boolean = false
) : BaseObservable() {

    @get:Bindable
    var loadingRecentChats: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingRecentChats)
        }

    @get:Bindable
    var loadingPosts: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingPosts)
        }
}
