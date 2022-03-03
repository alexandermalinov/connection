package com.connection.data.repository.user

import com.connection.data.api.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteSource: UserRemoteSource
) {

    interface RemoteSource {

        suspend fun loginAuth(
            email: String,
            password: String,
            onSuccess: (String?) -> Unit
        )

        suspend fun registerAuth(
            email: String,
            password: String,
            onSuccess: () -> Unit
        )

        suspend fun registerDB(user: User, onSuccess: (User) -> Unit)

        fun isUserLoggedIn(): Boolean

        fun connectSendBird(id: String)
    }

    suspend fun login(
        email: String,
        password: String,
        onSuccess: (String?) -> Unit
    ) {
        remoteSource.loginAuth(email, password, onSuccess)
    }

    suspend fun registerAuth(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        remoteSource.registerAuth(email, password, onSuccess)
    }

    suspend fun registerDB(user: User, onSuccess: (User) -> Unit) {
        remoteSource.registerDB(user, onSuccess)
    }

    fun connectSendBird(id: String) {
        remoteSource.connectSendBird(id)
    }

    fun isUserLoggedIn() = remoteSource.isUserLoggedIn()
}