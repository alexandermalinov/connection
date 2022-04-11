package com.connection.data.api.model

import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_EXTRA_DATA_CONNECTIONS
import com.connection.utils.common.Constants.USER_EXTRA_DATA_DESCRIPTION
import com.connection.utils.common.Constants.USER_EXTRA_DATA_PICTURE
import com.connection.utils.common.Constants.USER_EXTRA_DATA_USERNAME
import io.getstream.chat.android.client.models.User

data class UsersData(val users: List<UserData> = emptyList())

data class UserData(
    val id: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val username: String = EMPTY,
    val picture: String = EMPTY,
    val description: String = EMPTY,
    var connections: List<String> = emptyList()
): java.io.Serializable

fun UserData.toMap(id: String) = mapOf(
    "id" to id,
    "email" to email,
    "password" to password,
    "username" to username,
    "picture" to picture,
    "description" to description,
    "connections" to connections
)