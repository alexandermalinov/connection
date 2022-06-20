package com.connection.data.repository.post

import android.net.Uri
import com.connection.data.remote.response.post.Comment
import com.connection.data.remote.response.post.Like
import com.connection.data.remote.response.post.Post
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
import com.connection.utils.responsehandler.ResponseResultOk
import javax.inject.Inject

class PostRepository @Inject constructor(
    private val remote: PostRemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
    ---------------------------------------------------------------------------------------------*/
    interface RemoteSource {

        suspend fun createPost(
            post: Post,
            block: (Either<HttpError, ResponseResultOk>) -> Unit
        )

        suspend fun savePostPicture(
            picture: String,
            block: (Either<HttpError, Uri?>) -> Unit
        )

        suspend fun getUserPosts(
            id: String = EMPTY,
            block: (Either<HttpError, List<Post>>) -> Unit
        )

        suspend fun createComment(comment: Comment)

        suspend fun getPostComments(
            postId: String,
            block: (Either<HttpError, List<Comment>>) -> Unit
        )

        suspend fun like(
            postId: String,
            isLiked: Boolean,
            like: Like
        )

        suspend fun getLoggedUserPosts(block: (Either<HttpError, List<Post>>) -> Unit)
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    /**
     * Creates post in Firebase Realtime Database.
     * @param post is the post to be created
     * @param block returns either [HttpError] or [ResponseResultOk]. If result is [ResponseResultOk]
     * proceed executing ui logic
     */
    suspend fun createPost(
        post: Post,
        block: (Either<HttpError, ResponseResultOk>) -> Unit
    ) {
        remote.createPost(post, block)
    }

    /**
     * Saves the post's picture passed as an argument in Firebase Storage.
     * @param picture is the picture uri that will be saved in the storage
     * @param block returns either [HttpError] or the picture's url.
     */
    suspend fun savePostPicture(
        picture: String,
        block: (Either<HttpError, Uri?>) -> Unit
    ) {
        remote.savePostPicture(picture, block)
    }

    /**
     * Finds the user by the id and returns user all posts
     * @param id is the wanted user id
     * @param block returns either [HttpError] or user all posts.
     */
    suspend fun getUserPosts(
        id: String = EMPTY,
        block: (Either<HttpError, List<Post>>) -> Unit
    ) {
        remote.getUserPosts(id, block)
    }

    /**
     * Creates comment in Firebase Realtime Database.
     * @param comment is the comment to be created
     */
    suspend fun createComment(comment: Comment) {
        remote.createComment(comment)
    }

    /**
     * Finds the post by id and returns all of it's comments
     * @param postId is the post id that has to be found
     * @param block returns either [HttpError] or all post comment.
     */
    suspend fun getPostComments(
        postId: String,
        block: (Either<HttpError, List<Comment>>) -> Unit
    ) {
        remote.getPostComments(postId, block)
    }

    /**
     * Likes post that is found by id.
     * @param postId is the post id
     * @param isLiked checks whether the post has to be liked or not
     * @param like is object containing likeId, userId and userProfileImage
     */
    suspend fun like(
        postId: String,
        isLiked: Boolean,
        like: Like
    ) {
        remote.like(postId, isLiked, like)
    }

    /**
     * Returns the logged in user all posts
     * @param block returns either [HttpError] or list of the logged in user all posts
     */
    suspend fun getLoggedUserPosts(block: (Either<HttpError, List<Post>>) -> Unit) {
        remote.getLoggedUserPosts(block)
    }
}