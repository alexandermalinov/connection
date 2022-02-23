package com.connection.utils.common

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visible")
fun View.setVisibility(visible: Boolean) {
    if (visible) visibility = View.VISIBLE else View.GONE
}