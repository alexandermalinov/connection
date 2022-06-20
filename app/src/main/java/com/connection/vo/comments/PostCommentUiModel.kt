package com.connection.vo.comments

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY

data class PostCommentUiModel(
    val creatorId: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val postId: String = EMPTY,
    val postCreatedAt: String = EMPTY,
    val postDescription: String = EMPTY,
    val comments: List<PostCommentListItemUiModel> = emptyList()
) : BaseObservable() {

    @get:Bindable
    var commentText: String = EMPTY
        set(value) {
            field = value
            isPostButtonEnabled = field.isNotBlank()
            notifyPropertyChanged(BR.commentText)
        }

    @get:Bindable
    var isPostButtonEnabled: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.postButtonEnabled)
        }

    @get:Bindable
    var emptyComments: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.emptyComments)
        }
}
