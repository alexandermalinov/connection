package com.connection.data.api.model.user

import com.connection.utils.common.Constants.EMPTY

data class UsersData(val users: List<UserData> = emptyList())

data class UserData(
    val id: String = EMPTY,
    val email: String = EMPTY,
    val password: String = EMPTY,
    val username: String = EMPTY,
    val picture: String = EMPTY,
    val description: String = EMPTY,
    var connections: Map<String, String> = emptyMap()
) : java.io.Serializable

fun UserData.toMap(id: String) = mapOf(
    "id" to id,
    "email" to email,
    "password" to password,
    "username" to username,
    "picture" to picture,
    "description" to description,
    "connections" to connections
)