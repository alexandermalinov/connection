package com.connection.utils.common

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.connection.utils.common.Constants.INVALID_RES

fun Fragment.grantReadUriPermission(uri: Uri) {
    context?.contentResolver?.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION
    )
}

fun Fragment.setStatusBarColor(color: Int) {
    if (color != INVALID_RES) {
        requireActivity().window?.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        requireActivity().window?.statusBarColor = resources.getColor(color)
    }
}
