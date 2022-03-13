package com.connection.vo.connectiontab

import io.getstream.chat.android.client.models.Channel

data class ConnectionChatUiModel(
    val profileImage: String,
    val username: String,
    val lastMessage: String
)

fun Channel.toUiModel() = ConnectionChatUiModel(
    profileImage = extraData["channel_picture"].toString(),
    username =  extraData["channel_name"].toString(),
    lastMessage = "Hello my dear friend!"
)

fun List<Channel>.toUiModels() = map {
    it.toUiModel()
}