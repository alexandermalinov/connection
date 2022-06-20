package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.remote.response.user.UserData
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
            block: (Either<HttpError, ResponseResultOk>) -> Unit
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

        suspend fun getLoggedUser(block: (Either<HttpError, UserData?>) -> Unit)

        suspend fun logoutUser(block: (Either<HttpError, ResponseResultOk>) -> Unit)

        fun addConnection(userId: String, connections: Map<String, String>)

        fun addInvitation(userId: String, invite: Map<String, String>, type: InviteTypes)
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    /**
     * Logs in the user by email and password
     * @param email user email to be logged in with
     * @param password user password to be logged in with
     * @param block returns either [HttpError] or [ResponseResultOk]
     */
    suspend fun login(
        email: String,
        password: String,
        block: (Either<HttpError, ResponseResultOk>) -> Unit
    ) {
        remote.loginAuth(email, password, block)
    }

    /**
     * Registries new user in Firebase Authentication with email and password and returns newly generated ID
     * @param email user email to be registered with
     * @param password user password to be registered with
     * @param block returns either [HttpError] or newly generated ID
     */
    suspend fun registerAuth(
        email: String,
        password: String,
        block: (Either<HttpError, String>) -> Unit
    ) {
        remote.registerAuth(email, password, block)
    }

    /**
     * Registries the user passed as a param in Firebase Realtime Database
     * @param user the user to be registered
     * @param block returns either [HttpError] or [ResponseResultOk]
     */
    suspend fun registerDB(
        user: UserData,
        block: (Either<HttpError, ResponseResultOk>) -> Unit
    ) {
        remote.registerDB(user, block)
    }

    /**
     * Checks whether the user is logged in or not
     * @param block returns either [HttpError] or [ResponseResultOk]
     */
    suspend fun isUserLoggedIn(block: (Either<HttpError, ResponseResultOk>) -> Unit) {
        remote.isUserLoggedIn(block)
    }

    /**
     * Returns the uploaded in firebase storage image's url
     * @param uri the uri of the image to be uploaded
     * @param block returns either [HttpError] or image's url
     */
    suspend fun uploadImage(
        uri: Uri?,
        block: (Either<HttpError, Uri?>) -> Unit
    ) {
        remote.uploadImage(uri, block)
    }

    /**
     * Returns user searched by his id
     * @param id the searched user id
     * @param block returns either [HttpError] or the user
     */
    suspend fun getUser(
        id: String,
        block: (Either<HttpError, UserData?>) -> Unit
    ) = remote.getUser(id, block)

    /**
     * Returns all users available in the database
     * @param block returns either [HttpError] or list of all users
     */
    suspend fun getUsers(
        block: (Either<HttpError, List<UserData>>) -> Unit
    ) = remote.getUsers(block)


    /**
     * Returns the logged in user.
     * @param block returns either [HttpError] or the logged in user
     */
    suspend fun getLoggedUser(
        block: (Either<HttpError, UserData?>) -> Unit
    ) = remote.getLoggedUser(block)

    /**
     * Logouts the logged in used
     * @param block returns either [HttpError] or [ResponseResultOk]>
     */
    suspend fun logoutUser(block: (Either<HttpError, ResponseResultOk>) -> Unit) {
        remote.logoutUser(block)
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