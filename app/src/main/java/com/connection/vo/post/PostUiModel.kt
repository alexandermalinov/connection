package com.connection.vo.post

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.api.model.post.Comment
import com.connection.data.api.model.post.Post
import com.connection.data.api.model.user.UserData
import com.connection.utils.DateTimeFormatter
import com.connection.utils.common.Constants.EMPTY
import java.util.*

data class PostUiModel(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val picture: String = EMPTY,
    var description: String = EMPTY,
    val createdAt: String = EMPTY,
    val comments: List<Comment> = emptyList(),
    val likes: Map<String, String> = emptyMap()
) : BaseObservable() {

    @get:Bindable
    var isLiked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.liked)
        }
}

fun PostUiModel.toPost(picture: Uri?) = Post(
    id = UUID.randomUUID().toString(),
    creatorId = creatorId,
    creatorUsername = creatorUsername,
    creatorPicture = creatorPicture,
    description = description,
    picture = picture.toString(),
    comments = comments,
    createAt = System.currentTimeMillis(),
    likes = likes
)
