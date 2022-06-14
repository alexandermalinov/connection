package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.remote.model.user.UserData
import com.connection.data.api.remote.model.user.UsersData
import com.connection.ui.base.InviteTypes
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

        suspend fun getUser(id: String, onSuccess: (UserData?) -> Unit)

        suspend fun getUsers(onSuccess: (UsersData) -> Unit, onFailure: () -> Unit)

        suspend fun getLoggedUserId(): String

        suspend fun getLoggedUser(onSuccess: (UserData?) -> Unit)

        suspend fun logoutUser(onSuccess: () -> Unit)

        fun addConnection(userId: String, connections: Map<String, String>)

        fun addInvitation(userId: String, invite: Map<String, String>, type: InviteTypes)
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

    suspend fun getUser(
        id: String,
        onSuccess: (UserData?) -> Unit
    ) {
        remote.getUser(id, onSuccess)
    }

    suspend fun getUsers(
        onSuccess: (UsersData) -> Unit,
        onFailure: () -> Unit
    ) = remote.getUsers(onSuccess, onFailure)

    suspend fun getLoggedUser(onSuccess: (UserData?) -> Unit) {
        remote.getLoggedUser(onSuccess)
    }

    suspend fun logoutUser(onSuccess: () -> Unit) {
        remote.logoutUser(onSuccess)
    }

    fun addConnection(
        userId: String,
        connections: Map<String, String>
    ) {
        remote.addConnection(userId, connections)
    }

    fun addInvitation(userId: String, invite: Map<String, String>, type: InviteTypes) {
        remote.addInvitation(userId, invite, type)
    }
}