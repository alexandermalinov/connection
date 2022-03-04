package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.User
import com.connection.utils.common.Constants.EMPTY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sendbird.android.SendBird
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val storage: FirebaseStorage
) : UserRepository.RemoteSource {

    private fun User.toMap(id: String) = mapOf(
        "user_id" to id,
        "email" to email,
        "password" to password,
        "username" to username,
        "profile_picture" to profilePicture,
        "description" to description
    )

    override suspend fun loginAuth(
        email: String,
        password: String,
        onSuccess: (String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                if (login.isSuccessful)
                    onSuccess.invoke(login.result.user?.uid)
                else
                    Timber.e("error occurred: ${login.exception?.message}")
            }
    }

    override fun connectSendBird(id: String) {
        SendBird.connect(auth.currentUser?.uid) { sendbirdUser, error ->
            if (error != null)
                Timber.e("error occurred: ${error.message}")
        }
    }

    override fun registerDB(user: User, onSuccess: (User) -> Unit) {
        db.getReference("users/${auth.uid}")
            .push()
            .setValue(user.toMap(auth.uid ?: EMPTY))
            .addOnCompleteListener { register ->
                if (register.isSuccessful)
                    onSuccess.invoke(user)
            }
    }

    override suspend fun registerAuth(
        email: String,
        password: String,
        onSuccess: () -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { register ->
                if (register.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    Timber.e("error occurred: ${register.exception?.message}")
                }
            }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun uploadImage(uri: Uri?, onSuccess: (Uri?) -> Unit) {
        uri?.let {
            storage
                .getReference("/profile_image/${UUID.randomUUID()}")
                .apply {
                    putFile(it).addOnSuccessListener {
                        downloadUrl.addOnSuccessListener {
                            onSuccess.invoke(it)
                        }
                    }
                }
        }
    }

    override suspend fun getUser(id: String, onSuccess: (User?) -> Unit) {
        db
            .getReference("users")
            .child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess.invoke(snapshot.getValue(User::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }

    override fun getLoggedUserId(): String = auth.currentUser?.uid ?: EMPTY
}