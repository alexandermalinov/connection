package com.connection.utils.common

import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleGone")
fun View.setVisibility(show: Boolean) {
    if (show) visibility = View.VISIBLE else View.GONE
}

@BindingAdapter("showHide")
fun ContentLoadingProgressBar.showHide(show: Boolean) {
    if (show) show() else hide()
}