package com.connection.data.repository.user

import com.connection.data.api.response.User
import com.connection.utils.common.Constants.EMPTY
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    private val auth: FirebaseAuth
) : UserRepository.RemoteSource {

    override suspend fun logUser(
        user: User?,
        onSuccess: (User?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(
            user?.email ?: EMPTY,
            user?.password ?: EMPTY
        ).addOnCompleteListener { task ->
            if (task.isSuccessful)
                onSuccess(user)
        }
    }

    override suspend fun registerUser(
        user: User?,
        onSuccess: (User?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(
            user?.email ?: EMPTY,
            user?.password ?: EMPTY
        ).addOnCompleteListener { task ->
            if (task.isSuccessful)
                onSuccess(user)
        }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

}