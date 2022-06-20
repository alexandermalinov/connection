package com.connection.data.repository.post

import android.net.Uri
import com.connection.data.remote.response.post.Comment
import com.connection.data.remote.response.post.Like
import com.connection.data.remote.response.post.Post
import com.connection.data.remote.response.post.Posts
import com.connection.utils.common.Constants.EMPTY
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
            id: String = EMPTY,
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

        suspend fun getLoggedUserPosts(onSuccess: (Posts) -> Unit, onFailure: () -> Unit)
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
        id: String = EMPTY,
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

    suspend fun getLoggedUserPosts(onSuccess: (Posts) -> Unit, onFailure: () -> Unit) {
        remote.getLoggedUserPosts(onSuccess, onFailure)
    }
}