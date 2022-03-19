package com.connection.vo.alltabs

import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectiontab.ConnectionTabUiModel

data class AllTabsUiModel(
    val profileImage: String = EMPTY,
    val connections: List<ConnectionTabUiModel> = emptyList()
)