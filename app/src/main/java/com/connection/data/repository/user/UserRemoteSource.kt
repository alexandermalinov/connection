package com.connection.data.repository.user

import android.net.Uri
import com.connection.data.api.model.user.UserData
import com.connection.data.api.model.user.UsersData
import com.connection.data.api.model.user.toMap
import com.connection.utils.common.Constants.EMPTY
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
                if (login.isSuccessful)
                    onSuccess.invoke(login.result.user?.uid)
                else
                    Timber.e("error occurred: ${login.exception?.message}")
            }
    }

    override fun registerDB(user: UserData, onSuccess: (UserData) -> Unit) {
        db.getReference("/users/${auth.uid}")
            .setValue(user.toMap(auth.uid ?: EMPTY))
            .addOnSuccessListener {
                onSuccess.invoke(user)
            }
    }

    override suspend fun registerAuth(
        email: String,
        password: String,
        onSuccess: (String) -> Unit,
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { register ->
                onSuccess.invoke(register.user?.uid ?: EMPTY)
            }
    }

    override fun isUserLoggedIn(): Boolean = auth.currentUser != null

    override fun uploadImage(uri: Uri?, onSuccess: (Uri?) -> Unit) {
        uri?.let {
            storage
                .getReference("/profile_image/${UUID.randomUUID()}")
                .apply {
                    putFile(it).addOnSuccessListener {
                        downloadUrl.addOnSuccessListener {
                            onSuccess.invoke(it)
                        }
                    }
                }
        }
    }

    override fun getUser(
        id: String,
        onSuccess: (UserData?) -> Unit
    ) {
        db.getReference("users")
            .child(id)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    onSuccess.invoke(snapshot.getValue(UserData::class.java))
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
        db.getReference("users")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (user in snapshot.children) {
                        user.getValue(UserData::class.java)?.let {
                            users.add(it)
                        }
                    }
                    onSuccess.invoke(UsersData(users))
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.e("Error occurred: ${error.message}")
                }
            })
    }

    override suspend fun getLoggedUserId(): String = auth.currentUser?.uid ?: EMPTY

    // TODO ("optimize with auth.currentUser")
    override suspend fun getLoggedUser(onSuccess: (UserData?) -> Unit) {
        getUser(auth.currentUser?.uid ?: EMPTY) {
            onSuccess.invoke(it)
        }
    }

    // TODO ("fix this shit")
    override fun updateUser(
        userId: String,
        connections: Map<String, String>
    ) {
        db.getReference("users")
            .child(userId)
            .child("connections")
            .child(connections.keys.first())
            .setValue(connections.values.first())
    }

    override suspend fun logoutUser(onSuccess: () -> Unit) {
        auth.signOut()
        SendBird.disconnect {
            onSuccess()
        }
    }
}