package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.user.UserData
import com.connection.data.api.model.user.UsersData
import com.connection.data.api.model.user.toMap
import com.connection.utils.common.Constants.CONNECTIONS
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.SEARCH_POSTFIX
import com.connection.utils.common.Constants.USERNAME
import com.connection.utils.common.Constants.USERS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sendbird.android.SendBird
import timber.log.Timber
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
        onSuccess: (String?) -> Unit,
        onFailure: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { login ->
                if (login.isSuccessful) {
                    onSuccess(login.result.user?.uid)
                } else {
                    onFailure()
                    Timber.e("error occurred: ${login.exception?.message}")
                }
            }
    }

    override fun registerDB(
        user: UserData,
        onSuccess: (UserData) -> Unit,
        onFailure: () -> Unit
    ) {
        db.getReference("/users/${auth.uid}")
            .setValue(user.toMap(auth.uid ?: EMPTY))
            .addOnSuccessListener { onSuccess(user) }
            .addOnFailureListener { onFailure() }
    }

    override suspend fun registerAuth(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
        onFailure: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { onSuccess(it.user?.uid ?: EMPTY) }
            .addOnFailureListener { onFailure() }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun uploadImage(
        uri: Uri?,
        onSuccess: (Uri?) -> Unit,
        onFailure: () -> Unit
    ) {
        uri?.let {
            storage
                .getReference("/profile_image/${UUID.randomUUID()}")
                .apply {
                    putFile(it).addOnSuccessListener {
                        downloadUrl
                            .addOnSuccessListener { onSuccess(it) }
                            .addOnFailureListener { onFailure() }
                    }
                }
        }
    }

    override fun getUser(
        id: String,
        onSuccess: (UserData?) -> Unit
    ) {
        db.getReference(USERS)
            .child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess(snapshot.getValue(UserData::class.java))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }

    override suspend fun getUsers(
        onSuccess: (UsersData) -> Unit,
        onFailure: () -> Unit
    ) {
        val users: MutableList<UserData> = mutableListOf()
        db.getReference(USERS)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (user in snapshot.children)
                        user.getValue(UserData::class.java)?.let { users.add(it) }
                    onSuccess(UsersData(users))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }

    override suspend fun getLoggedUserId(): String = auth.currentUser?.uid ?: EMPTY

    override suspend fun getLoggedUser(onSuccess: (UserData?) -> Unit) {
        getUser(auth.currentUser?.uid ?: EMPTY) {
            onSuccess(it)
        }
    }

    override fun updateUser(
        userId: String,
        connections: Map<String, String>
    ) {
        db.getReference(USERS)
            .child(userId)
            .child(CONNECTIONS)
            .child(connections.keys.first())
            .setValue(connections.values.first())
    }

    override suspend fun logoutUser(onSuccess: () -> Unit) {
        auth.signOut()
        SendBird.disconnect { onSuccess() }
    }

    /**
     * The character \uf8ff used in the query is a very high code point in the Unicode range.
     * Because it is after most regular characters in Unicode,
     * the query matches all values that start with searchText.
     */
    override suspend fun searchUsers(
        searchText: String,
        onSuccess: (List<UserData>) -> Unit,
        onFailure: () -> Unit
    ) {
        val users: MutableList<UserData> = mutableListOf()
        db.getReference(USERS)
            .orderByChild(USERNAME)
            .startAt(searchText)
            .endAt(searchText + SEARCH_POSTFIX)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (user in snapshot.children)
                        user.getValue(UserData::class.java)?.let { users.add(it) }
                    onSuccess(users)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }
}