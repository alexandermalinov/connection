package com.connection.data.repository.chat

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.sendbird.android.SendBird
import timber.log.Timber
import javax.inject.Inject

class ChatTabRemoteSource @Inject constructor(
    private val auth: FirebaseAuth,
    private val db: FirebaseDatabase,
    private val storage: FirebaseStorage
) : ChatTabRepository.RemoteSource {

    override fun connectSendBird(id: String) {
        SendBird.connect(auth.currentUser?.uid) { sendbirdUser, error ->
            if (error != null)
                Timber.e("error occurred: ${error.message}")
        }
    }
}