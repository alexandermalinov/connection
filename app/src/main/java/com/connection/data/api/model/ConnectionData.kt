package com.connection.data.api.model

import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.serialization.Serializable

@IgnoreExtraProperties
data class ConnectionData(
    val id: String,
    val picture: String
)

fun ConnectionData.toMap() = mapOf(
    "id" to id,
    "picture" to picture,
)
