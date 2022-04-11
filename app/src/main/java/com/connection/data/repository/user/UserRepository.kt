package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.UserData
import com.connection.data.api.model.UsersData
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remote: UserRemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
     ---------------------------------------------------------------------------------------------*/
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

        fun getUser(
            id: String,
            onSuccess: (UserData?) -> Unit
        )

        suspend fun getUsers(
            onSuccess: (UsersData) -> Unit,
            onFailure: () -> Unit
        )

        suspend fun getLoggedUserId(): String

        suspend fun getLoggedUser(onSuccess: (UserData?) -> Unit)

        fun updateUser(
            userId: String,
            connections: List<String>
        )

        suspend fun logoutUser(onSuccess: () -> Unit)
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    suspend fun login(
        email: String,
        password: String,
        onSuccess: (String?) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.loginAuth(email, password, onSuccess, onFailure)
    }

    suspend fun registerAuth(
        email: String,
        password: String,
        onSuccess: (String) -> Unit
    ) {
        remote.registerAuth(email, password, onSuccess)
    }

    fun registerDB(
        user: UserData,
        onSuccess: (UserData) -> Unit
    ) {
        remote.registerDB(user, onSuccess)
    }

    fun isUserLoggedIn() = remote.isUserLoggedIn()

    fun uploadImage(
        uri: Uri?,
        onSuccess: (Uri?) -> Unit
    ) {
        remote.uploadImage(uri, onSuccess)
    }

    fun getUser(
        id: String,
        onSuccess: (UserData?) -> Unit
    ) {
        remote.getUser(id, onSuccess)
    }

    suspend fun getUsers(
        onSuccess: (UsersData) -> Unit,
        onFailure: () -> Unit
    ) = remote.getUsers(onSuccess, onFailure)

    suspend fun getLoggedUserId(): String = remote.getLoggedUserId()

    suspend fun getLoggedUser(onSuccess: (UserData?) -> Unit) {
        remote.getLoggedUser(onSuccess)
    }

    fun updateUser(
        userId: String,
        connections: List<String>
    ) {
        remote.updateUser(userId, connections)
    }

    suspend fun logoutUser(onSuccess: () -> Unit) {
        remote.logoutUser(onSuccess)
    }
}