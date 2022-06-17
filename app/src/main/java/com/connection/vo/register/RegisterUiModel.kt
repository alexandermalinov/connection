package com.connection.vo.register

import android.net.Uri
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.INVALID_RES
import java.util.*

data class RegisterUiModel(
    val id: String = EMPTY,
    var email: String = EMPTY,
    var username: String = EMPTY,
    var password: String = EMPTY
) : BaseObservable() {

    @get:Bindable
    var profilePicture: Uri? = Uri.EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.profilePicture)
        }

    @get:Bindable
    var emailError: Int = INVALID_RES
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailError)
        }

    @get:Bindable
    var usernameError: Int = INVALID_RES
        set(value) {
            field = value
            notifyPropertyChanged(BR.usernameError)
        }

    @get:Bindable
    var passwordError: Int = INVALID_RES
        set(value) {
            field = value
            notifyPropertyChanged(BR.passwordError)
        }

    @get:Bindable
    var loading: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }
}