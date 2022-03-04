package com.connection.utils.common

import android.content.Intent
import android.net.Uri
import androidx.fragment.app.Fragment

fun Fragment.grantReadUriPermission(uri: Uri) {
    context?.contentResolver?.takePersistableUriPermission(
        uri,
        Intent.FLAG_GRANT_READ_URI_PERMISSION
    )
}
