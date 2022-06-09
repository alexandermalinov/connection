package com.connection.data.repository.chattab

import com.connection.data.api.model.user.UserData
import com.connection.ui.base.ConnectionStatus
import com.sendbird.android.GroupChannel
import com.sendbird.android.User
import javax.inject.Inject

class ChatTabRepository @Inject constructor(
    private val remote: RemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
     ---------------------------------------------------------------------------------------------*/
    interface RemoteSource {

        fun connectUser(
            user: UserData,
            onFailure: () -> Unit
        )

        suspend fun fetchChannels(
            filter: ConnectionStatus,
            onSuccess: (List<GroupChannel>) -> Unit,
            onFailure: () -> Unit
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    suspend fun fetchChannels(
        filter: ConnectionStatus,
        onSuccess: (List<GroupChannel>) -> Unit,
        onFailure: () -> Unit
    ) {
        remote.fetchChannels(filter, onSuccess, onFailure)
    }

    fun connectUser(
        user: UserData,
        onFailure: () -> Unit
    ) {
        remote.connectUser(user, onFailure)
    }
}