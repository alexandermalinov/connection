package com.connection.vo.alltabs

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectiontab.ConnectionChatUiModel

data class AllTabsUiModel(
    val profileImage: String = EMPTY,
    val connections: List<ConnectionChatUiModel> = emptyList()
)