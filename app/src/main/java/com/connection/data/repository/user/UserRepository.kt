package com.connection.data.repository.user

import com.connection.data.api.response.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteSource: UserRemoteSource
) {

    interface RemoteSource {

        suspend fun logUser(
            user: User?,
            onSuccess: (User?) -> Unit
        )

        suspend fun registerUser(
            user: User?,
            onSuccess: (User?) -> Unit
        )

        fun isUserLoggedIn(): Boolean
    }

    suspend fun logUser(
        user: User?,
        onSuccess: (User?) -> Unit
    ) {
        remoteSource.logUser(user, onSuccess)
    }

    suspend fun registerUser(
        user: User?,
        onSuccess: (User?) -> Unit
    ) {
        remoteSource.registerUser(user, onSuccess)
    }

    fun isUserLoggedIn() = remoteSource.isUserLoggedIn()
}