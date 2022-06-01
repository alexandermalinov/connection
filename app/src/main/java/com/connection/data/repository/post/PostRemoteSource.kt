package com.connection.data.repository.post

import android.net.Uri
import androidx.core.net.toUri
import com.connection.data.api.model.post.Post
import com.connection.data.api.model.post.Posts
import com.connection.data.api.model.user.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class PostRemoteSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val storage: FirebaseStorage
) : PostRepository.RemoteSource {

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun createPost(post: Post) {
        db.getReference("posts")
            .push()
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
        db.getReference("posts")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    snapshot.children.forEach { post ->
                        post.getValue(Post::class.java)?.let { posts.add(it) }
                    }
                    onSuccess.invoke(Posts(posts))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }
}