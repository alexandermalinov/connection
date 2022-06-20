package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.remote.response.user.UserData
import com.connection.data.remote.response.user.toMap
import com.connection.ui.base.InviteTypes
import com.connection.utils.common.Constants.CACHED_USER_ID
import com.connection.utils.common.Constants.CONNECTIONS
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USERS
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
import com.connection.utils.responsehandler.ResponseResultOk
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sendbird.android.SendBird
import java.util.*
import javax.inject.Inject

class UserRemoteSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val storage: FirebaseStorage
) : UserRepository.RemoteSource {

    /* --------------------------------------------------------------------------------------------
     * Override
     ---------------------------------------------------------------------------------------------*/
    override suspend fun loginAuth(
        email: String,
        password: String,
        block: (Either<HttpError, String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                block.invoke(Either.right(login.result.user?.uid ?: EMPTY))
            }
            .addOnFailureListener { error ->
                block.invoke(Either.left(HttpError(serverMessage = error.message)))
            }
    }

    override suspend fun registerDB(
        user: UserData,
        block: (Either<HttpError, ResponseResultOk>) -> Unit
    ) {
        db.getReference("/users/${auth.uid}")
            .setValue(user.toMap(auth.uid ?: EMPTY))
            .addOnSuccessListener { block.invoke(Either.right(ResponseResultOk)) }
            .addOnFailureListener { block.invoke(Either.left(HttpError())) }
    }

    override suspend fun registerAuth(
        email: String,
        password: String,
        block: (Either<HttpError, String>) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { block.invoke(Either.right(it.user?.uid ?: EMPTY)) }
            .addOnFailureListener { block.invoke(Either.left(HttpError(serverMessage = it.message))) }
    }

    /**
     * This is temporary until a solution is find to this problem:
     * If the user was deleted on another device and the local token has not refreshed.
     * In this case, getCurrentUser will return a non-null FirebaseUser
     * but the underlying token is not valid
     */
    override suspend fun isUserLoggedIn(
        block: (Either<HttpError, ResponseResultOk>) -> Unit
    ) {
        auth.addAuthStateListener { state ->
            if (state.currentUser != null && state.currentUser?.uid != CACHED_USER_ID)
                block.invoke(Either.right(ResponseResultOk))
            else
                block.invoke(Either.left(HttpError()))
        }
    }

    override suspend fun uploadImage(
        uri: Uri?,
        block: (Either<HttpError, Uri?>) -> Unit
    ) {
        uri?.let {
            storage
                .getReference("/profile_image/${UUID.randomUUID()}")
                .apply {
                    putFile(it).addOnSuccessListener {
                        downloadUrl
                            .addOnSuccessListener { block.invoke(Either.right(it)) }
                            .addOnFailureListener { block.invoke(Either.left(HttpError())) }
                    }
                }
        }
    }

    override suspend fun getUser(
        id: String,
        block: (Either<HttpError, UserData?>) -> Unit
    ) {
        db.getReference(USERS)
            .child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    block.invoke(Either.right(snapshot.getValue(UserData::class.java)))
                }

                override fun onCancelled(error: DatabaseError) {
                    block.invoke(Either.left(HttpError(serverMessage = error.message)))
                }
            })
    }

    override suspend fun getUsers(block: (Either<HttpError, List<UserData>>) -> Unit) {
        val users: MutableList<UserData> = mutableListOf()
        db.getReference(USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (user in snapshot.children)
                        user.getValue(UserData::class.java)?.let { users.add(it) }
                    block.invoke(Either.right(users))
                }

                override fun onCancelled(error: DatabaseError) {
                    block.invoke(Either.left(HttpError(serverMessage = error.message)))
                }
            })
    }

    override suspend fun getLoggedUserId(): String = auth.currentUser?.uid ?: EMPTY

    override suspend fun getLoggedUser(block: (Either<HttpError, UserData?>) -> Unit) {
        getUser(auth.currentUser?.uid ?: EMPTY) { either ->
            either.fold({ error ->
                block.invoke(Either.left(HttpError()))
            }, { user ->
                block.invoke(Either.right(user))
            })
        }
    }

    override suspend fun logoutUser(onSuccess: () -> Unit) {
        auth.signOut()
        SendBird.disconnect { onSuccess() }
    }

    override fun addConnection(
        userId: String,
        connections: Map<String, String>
    ) {
        db.getReference(USERS)
            .child(userId)
            .child(CONNECTIONS)
            .child(connections.keys.first())
            .setValue(connections.values.first())
    }

    override fun addInvitation(
        userId: String,
        invite: Map<String, String>,
        type: InviteTypes
    ) {
        db.getReference(USERS)
            .child(userId)
            .child(type.name)
            .child(invite.keys.first())
            .setValue(invite.values.first())
    }
}