package com.connection.data.repository.user

import com.connection.data.api.model.User
import com.connection.utils.common.Constants.EMPTY
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sendbird.android.SendBird
import timber.log.Timber
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase
) : UserRepository.RemoteSource {

    private fun User.toMap(id: String) = mapOf(
        "user_id" to id,
        "email" to email,
        "password" to password,
        "username" to username,
        "profile_picture" to profilePicture,
        "description" to description,
        "interests" to interests
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

    override suspend fun registerDB(user: User, onSuccess: (User) -> Unit) {
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
}