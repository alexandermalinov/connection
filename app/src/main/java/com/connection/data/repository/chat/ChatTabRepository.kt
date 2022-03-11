package com.connection.data.repository.chat

import com.connection.data.repository.user.UserRemoteSource
import javax.inject.Inject

class ChatTabRepository @Inject constructor(
    private val remoteSource: RemoteSource
) {


    interface RemoteSource {

        fun connectSendBird(id: String)
    }

    fun connectSendBird(id: String) {
        remoteSource.connectSendBird(id)
    }
}