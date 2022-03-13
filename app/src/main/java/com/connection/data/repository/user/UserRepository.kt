package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.UserData
import com.connection.data.api.model.UsersData
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
            onSuccess: (String) -> Unit
        )

        fun registerDB(
            user: UserData,
            onSuccess: (UserData) -> Unit
        )

        fun isUserLoggedIn(): Boolean

        fun uploadImage(
            uri: Uri?,
            onSuccess: (Uri?) -> Unit
        )

        suspend fun getUser(
            id: String,
            onSuccess: (UserData?) -> Unit
        )

        suspend fun getUsers(
            onSuccess: (UsersData?) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun getLoggedUserId(): String
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
        onSuccess: (String) -> Unit
    ) {
        remoteSource.registerAuth(email, password, onSuccess)
    }

    fun registerDB(
        user: UserData,
        onSuccess: (UserData) -> Unit
    ) {
        remoteSource.registerDB(user, onSuccess)
    }

    fun isUserLoggedIn() = remoteSource.isUserLoggedIn()

    fun uploadImage(
        uri: Uri?,
        onSuccess: (Uri?) -> Unit
    ) {
        remoteSource.uploadImage(uri, onSuccess)
    }

    suspend fun getUser(
        id: String,
        onSuccess: (UserData?) -> Unit
    ) {
        remoteSource.getUser(id, onSuccess)
    }

    suspend fun getUsers(
        onSuccess: (UsersData?) -> Unit,
        onFailure: () -> Unit
    ) = remoteSource.getUsers(onSuccess, onFailure)

    suspend fun getLoggedUserId(): String = remoteSource.getLoggedUserId()

}