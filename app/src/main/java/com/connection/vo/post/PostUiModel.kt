package com.connection.vo.post

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.data.remote.response.post.Post
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectionchat.HeaderUiModel
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class PostUiModel(
    val id: String = EMPTY,
    val creatorId: String = EMPTY,
    val creatorUsername: String = EMPTY,
    val creatorPicture: String = EMPTY,
    val picture: String = EMPTY,
    var description: String = EMPTY,
    val createdAt: String = EMPTY,
    val commentsCount: String = EMPTY,
    val likes: Map<String, String> = emptyMap(),
    val isConnectVisible: Boolean = false
) : BaseObservable(), java.io.Serializable {

    @get:Bindable
    var isLiked: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.liked)
        }

    @get:Bindable
    var likesCount: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.likesCount)
        }

    @get:Bindable
    var loadingCreatePost: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingCreatePost)
        }
}

fun PostUiModel.toPost(picture: Uri?) = Post(
    id = UUID.randomUUID().toString(),
    creatorId = creatorId,
    creatorUsername = creatorUsername,
    creatorPicture = creatorPicture,
    description = description,
    picture = picture.toString(),
    createAt = System.currentTimeMillis(),
    likes = likes
)

fun PostUiModel.toUiModel() = HeaderUiModel(
    senderId = creatorId,
    profilePicture = creatorPicture,
    username = creatorUsername
)
