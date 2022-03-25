package com.connection.vo.alltabs

import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import io.getstream.chat.android.client.models.User

data class FavouriteConnectionUiModel(
    val favouriteConnections: List<FavouriteConnectionListItemUiModel> = emptyList()
)
