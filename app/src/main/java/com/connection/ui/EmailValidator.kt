package com.connection.ui

import android.util.Patterns


fun String.isEmailValid() = !isNullOrBlank() && validateEmail()

private fun String.validateEmail() = Patterns
    .EMAIL_ADDRESS
    .matcher(this)
    .matches()
