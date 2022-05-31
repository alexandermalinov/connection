package com.connection.vo.post.createpost

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.api.model.post.Comment
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY

data class CreatePostUiModel(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val picture: String = EMPTY,
    val createAt: String = DateTimeFormatter.formatDayOfYear(System.currentTimeMillis()),
    val comments: List<Comment> = emptyList()
) : BaseObservable() {

    @get:Bindable
    var title: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.title)
        }

    @get:Bindable
    var description: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

}
