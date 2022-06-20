package com.connection.data.repository.chattab

import com.connection.data.remote.response.user.UserData
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.responsehandler.Either
import com.connection.utils.responsehandler.HttpError
import com.sendbird.android.GroupChannel
import javax.inject.Inject

class ChatTabRepository @Inject constructor(
    private val remote: RemoteSource
) {

    /* --------------------------------------------------------------------------------------------
     * Sources
     ---------------------------------------------------------------------------------------------*/
    interface RemoteSource {

        suspend fun connectUser(
            user: UserData,
            onFailure: () -> Unit
        )

        suspend fun fetchChannels(
            filter: ConnectionStatus,
            block: (Either<HttpError, List<GroupChannel>>) -> Unit
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
     ---------------------------------------------------------------------------------------------*/
    suspend fun fetchChannels(
        filter: ConnectionStatus,
        block: (Either<HttpError, List<GroupChannel>>) -> Unit
    ) {
        remote.fetchChannels(filter, block)
    }

    suspend fun connectUser(
        user: UserData,
        onFailure: () -> Unit
    ) {
        remote.connectUser(user, onFailure)
    }
}