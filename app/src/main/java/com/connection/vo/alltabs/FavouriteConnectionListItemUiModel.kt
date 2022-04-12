package com.connection.vo.alltabs

import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY
import io.getstream.chat.android.client.models.User

data class FavouriteConnectionListItemUiModel(
    val image: String = EMPTY,
    val username: String = EMPTY,
    val online: Boolean = false
)

fun User.toUiModel() = FavouriteConnectionListItemUiModel(
    image = extraData[Constants.USER_EXTRA_DATA_PICTURE].toString(),
    username = extraData[Constants.USER_EXTRA_DATA_USERNAME].toString(),
    online = online
)