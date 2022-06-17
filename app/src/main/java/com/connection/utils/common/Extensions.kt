package com.connection.utils.common

import android.content.Intent
import android.net.Uri
import android.text.Editable
import android.text.TextWatcher
import android.view.WindowManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.connection.utils.common.Constants.INVALID_RES
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

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

fun Fragment.setStatusBarTransparent() {
    requireActivity().window.setFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

fun Fragment.removeTransparentStatusBar() {
    requireActivity().window.clearFlags(
        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
    )
}

interface TextChangesCallback {
    fun textChanges(flow: Flow<CharSequence>)
}

@ExperimentalCoroutinesApi
fun EditText.textChanges(): Flow<CharSequence> = callbackFlow {
    val listener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) = Unit

        override fun afterTextChanged(p0: Editable?) = Unit

        override fun onTextChanged(p0: CharSequence, p1: Int, p2: Int, p3: Int) {
            trySend(p0).isSuccess
        }
    }
    addTextChangedListener(listener)
    awaitClose { removeTextChangedListener(listener) }
}