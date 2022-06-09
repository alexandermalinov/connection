package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.user.UserData
import com.connection.data.api.model.user.UsersData
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
            onSuccess: (String) -> Unit,
            onFailure: () -> Unit
        )

        fun registerDB(
            user: UserData,
            onSuccess: (UserData) -> Unit,
            onFailure: () -> Unit
        )

        fun isUserLoggedIn(): Boolean

        fun uploadImage(
            uri: Uri?,
            onSuccess: (Uri?) -> Unit,
            onFailure: () -> Unit
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
            connections: Map<String, String>
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
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.registerAuth(email, password, onSuccess, onFailure)
    }

    fun registerDB(
        user: UserData,
        onSuccess: (UserData) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.registerDB(user, onSuccess, onFailure)
    }

    fun isUserLoggedIn() = remote.isUserLoggedIn()

    fun uploadImage(
        uri: Uri?,
        onSuccess: (Uri?) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.uploadImage(uri, onSuccess, onFailure)
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
        connections: Map<String, String>
    ) {
        remote.updateUser(userId, connections)
    }

    suspend fun logoutUser(onSuccess: () -> Unit) {
        remote.logoutUser(onSuccess)
    }
}