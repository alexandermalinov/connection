package com.connection.vo.connectionchat

import com.connection.utils.common.Constants.EMPTY
import kotlinx.serialization.Serializable

@Serializable
data class HeaderUiModel(
    val id: String = EMPTY,
    val profilePicture: String = EMPTY,
    val username: String = EMPTY,
    val isOnline: Boolean = false
) : java.io.Serializable
