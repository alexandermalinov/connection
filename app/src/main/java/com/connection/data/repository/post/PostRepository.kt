package com.connection.data.repository.post

import android.net.Uri
import com.connection.data.api.model.post.Comment
import com.connection.data.api.model.post.Like
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

        suspend fun createComment(comment: Comment)

        suspend fun getPostComments(
            postId: String,
            onSuccess: (List<Comment>) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun like(
            postId: String,
            isLiked: Boolean,
            like: Like
        )
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

    suspend fun createComment(comment: Comment) {
        remote.createComment(comment)
    }

    suspend fun getPostComments(
        postId: String,
        onSuccess: (List<Comment>) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.getPostComments(postId, onSuccess, onFailure)
    }

    suspend fun like(
        postId: String,
        isLiked: Boolean,
        like: Like
    ) {
        remote.like(postId, isLiked, like)
    }
}