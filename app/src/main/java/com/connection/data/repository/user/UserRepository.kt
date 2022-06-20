package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.remote.response.user.UserData
import com.connection.data.remote.response.user.UsersData
import com.connection.ui.base.InviteTypes
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
import com.connection.utils.responsehandler.ResponseResultOk
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
            block: (Either<HttpError, String>) -> Unit
        )

        suspend fun registerAuth(
            email: String,
            password: String,
            block: (Either<HttpError, String>) -> Unit
        )

        suspend fun registerDB(
            user: UserData,
            block: (Either<HttpError, ResponseResultOk>) -> Unit
        )

        suspend fun isUserLoggedIn(block: (Either<HttpError, ResponseResultOk>) -> Unit)

        suspend fun uploadImage(
            uri: Uri?,
            block: (Either<HttpError, Uri?>) -> Unit
        )

        suspend fun getUser(
            id: String,
            block: (Either<HttpError, UserData?>) -> Unit
        )

        suspend fun getUsers(block: (Either<HttpError, List<UserData>>) -> Unit)

        suspend fun getLoggedUserId(): String

        suspend fun getLoggedUser(block: (Either<HttpError, UserData?>) -> Unit)

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
        block: (Either<HttpError, String>) -> Unit
    ) {
        remote.loginAuth(email, password, block)
    }

    suspend fun registerAuth(
        email: String,
        password: String,
        block: (Either<HttpError, String>) -> Unit
    ) {
        remote.registerAuth(email, password, block)
    }

    suspend fun registerDB(
        user: UserData,
        block: (Either<HttpError, ResponseResultOk>) -> Unit
    ) {
        remote.registerDB(user, block)
    }

    suspend fun isUserLoggedIn(block: (Either<HttpError, ResponseResultOk>) -> Unit) {
        remote.isUserLoggedIn(block)
    }

    suspend fun uploadImage(
        uri: Uri?,
        block: (Either<HttpError, Uri?>) -> Unit
    ) {
        remote.uploadImage(uri, block)
    }

    suspend fun getUser(
        id: String,
        block: (Either<HttpError, UserData?>) -> Unit
    ) = remote.getUser(id, block)

    suspend fun getUsers(
        block: (Either<HttpError, List<UserData>>) -> Unit
    ) = remote.getUsers(block)

    suspend fun getLoggedUser(
        block: (Either<HttpError, UserData?>) -> Unit
    ) = remote.getLoggedUser(block)

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