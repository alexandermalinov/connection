package com.connection.ui

import android.util.Patterns

/* --------------------------------------------------------------------------------------------
 * Exposed
---------------------------------------------------------------------------------------------*/
fun String.isEmailValid() = !isNullOrBlank() && validateEmail()

fun String.isPasswordValid() = isNotBlank() && length > 6

fun String.isUsernameValid() = isNotBlank()

/* --------------------------------------------------------------------------------------------
 * Private
---------------------------------------------------------------------------------------------*/
private fun String.validateEmail() = Patterns
    .EMAIL_ADDRESS
    .matcher(this)
    .matches()
