package com.connection.data.repository.post

import android.net.Uri
import androidx.core.net.toUri
import com.connection.data.api.model.post.Comment
import com.connection.data.api.model.post.Like
import com.connection.data.api.model.post.Post
import com.connection.data.api.model.post.Posts
import com.connection.utils.common.Constants.COMMENTS
import com.connection.utils.common.Constants.POSTS
import com.connection.utils.common.Constants.POST_LIKES
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class PostRemoteSource @Inject constructor(
    private val db: FirebaseDatabase,
    private val storage: FirebaseStorage
) : PostRepository.RemoteSource {

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun createPost(post: Post) {
        db.getReference(POSTS)
            .child(post.id)
            .setValue(post)
    }

    override suspend fun savePostPicture(picture: String, onSuccess: (Uri?) -> Unit) {
        storage
            .getReference("/post_pictures/${UUID.randomUUID()}")
            .apply {
                putFile(picture.toUri()).addOnSuccessListener {
                    downloadUrl.addOnSuccessListener {
                        onSuccess.invoke(it)
                    }
                }
            }
    }

    override suspend fun getUserPosts(
        id: String,
        onSuccess: (Posts) -> Unit,
        onFailure: () -> Unit
    ) {
        val posts = mutableListOf<Post>()
        db.getReference(POSTS)
            .orderByChild("createAt")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { post ->
                        post.getValue(Post::class.java)?.let { posts.add(it) }
                    }
                    posts.sortByDescending { it.createAt }
                    onSuccess.invoke(Posts(posts))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }

    override suspend fun createComment(comment: Comment) {
        db.getReference(POSTS)
            .child(comment.postId)
            .child(COMMENTS)
            .push()
            .setValue(comment)
    }

    override suspend fun getPostComments(
        postId: String,
        onSuccess: (List<Comment>) -> Unit,
        onFailure: () -> Unit
    ) {
        val comments = mutableListOf<Comment>()
        db.getReference(POSTS)
            .child(postId)
            .child(COMMENTS)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { post ->
                        post.getValue(Comment::class.java)?.let { comments.add(it) }
                    }
                    comments.sortByDescending { it.createdAt }
                    onSuccess.invoke(comments)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }

    override suspend fun like(
        postId: String,
        isLiked: Boolean,
        like: Like
    ) {
        db.getReference(POSTS)
            .child(postId)
            .child(POST_LIKES)
            .child(like.userId)
            .let {
                if (isLiked)
                    it.setValue(like.userProfilePicture)
                else
                    it.removeValue()
            }
    }
}