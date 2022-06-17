package com.connection.utils.common

import androidx.annotation.StringRes
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.INVALID_RES

data class TextRes(
    val text: String? = EMPTY,
    @StringRes
    val textResource: Int = INVALID_RES
)