package com.connection.data.repository.post

import android.net.Uri
import com.connection.data.api.model.post.Post
import com.connection.data.api.model.post.Posts
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val remote: PostRemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
    ---------------------------------------------------------------------------------------------*/
    interface RemoteSource {

        fun createPost(post: Post)

        suspend fun savePostPicture(picture: String, onSuccess: (Uri?) -> Unit)

        suspend fun getUserPosts(
            id: String,
            onSuccess: (Posts) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun like(id: String, isLiked: Boolean)
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    fun createPost(post: Post) {
        remote.createPost(post)
    }

    suspend fun savePostPicture(picture: String, onSuccess: (Uri?) -> Unit) {
        remote.savePostPicture(picture, onSuccess)
    }

    suspend fun getUserPosts(
        id: String,
        onSuccess: (Posts) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.getUserPosts(id, onSuccess, onFailure)
    }

    suspend fun like(id: String, isLiked: Boolean) {
        remote.like(id, isLiked)
    }
}