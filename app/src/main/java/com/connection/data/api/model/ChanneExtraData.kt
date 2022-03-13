package com.connection.data.api.model

import com.connection.utils.common.Constants.EMPTY

data class ChannelExtraData(
    val channelName: String = EMPTY,
    val channelPicture: String = EMPTY
)

fun ChannelExtraData.toExtraData() = mutableMapOf(
    "channel_name" to channelName,
    "channel_picture" to channelPicture
)
