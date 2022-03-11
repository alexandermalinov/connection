package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.User
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteSource: UserRemoteSource
) {

    interface RemoteSource {

        suspend fun loginAuth(
            email: String,
            password: String,
            onSuccess: (String?) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun registerAuth(
            email: String,
            password: String,
            onSuccess: () -> Unit
        )

        suspend fun registerDB(
            user: User,
            onSuccess: (User) -> Unit
        )

        fun isUserLoggedIn(): Boolean

        suspend fun uploadImage(
            uri: Uri?,
            onSuccess: (Uri?) -> Unit
        )

        suspend fun getUser(
            id: String,
            onSuccess: (User?) -> Unit
        )

        fun getLoggedUserId(): String
    }

    suspend fun login(
        email: String,
        password: String,
        onSuccess: (String?) -> Unit,
        onFailure: () -> Unit
    ) {
        remoteSource.loginAuth(email, password, onSuccess, onFailure)
    }

    suspend fun registerAuth(
        email: String,
        password: String,
        onSuccess: () -> Unit
    ) {
        remoteSource.registerAuth(email, password, onSuccess)
    }

    suspend fun registerDB(
        user: User,
        onSuccess: (User) -> Unit
    ) {
        remoteSource.registerDB(user, onSuccess)
    }

    fun isUserLoggedIn() = remoteSource.isUserLoggedIn()

    suspend fun uploadImage(
        uri: Uri?,
        onSuccess: (Uri?) -> Unit
    ) {
        remoteSource.uploadImage(uri, onSuccess)
    }

    suspend fun getUser(
        id: String,
        onSuccess: (User?) -> Unit
    ) {
        remoteSource.getUser(id, onSuccess)
    }

    fun getLoggedUserId(): String = remoteSource.getLoggedUserId()

}