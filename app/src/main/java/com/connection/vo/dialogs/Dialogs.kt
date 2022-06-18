package com.connection.vo.dialogs

import androidx.annotation.StringRes
import com.connection.R
import com.connection.utils.common.Constants.INVALID_RES

/**
 * Base dialog class that can be inherited by all types of dialogs
 */
sealed class Dialog

/**
 * Alert dialog with title, message and two optional buttons
 */
open class TitleMessageDialog(
    @StringRes val title: Int = INVALID_RES,
    @StringRes val message: Int = INVALID_RES,
    @StringRes val positiveButtonText: Int = R.string.ok,
    @StringRes val negativeButtonText: Int = R.string.cancel,
    val positiveButtonClickListener: (() -> Unit)? = null,
    val negativeButtonClickListener: (() -> Unit)? = null,
    val cancelable: Boolean = true
) : Dialog()