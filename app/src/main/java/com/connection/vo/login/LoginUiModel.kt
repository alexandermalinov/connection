package com.connection.vo.login

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.EMPTY

data class LoginUiModel(
    var email: String = EMPTY,
    var password: String = EMPTY
) : BaseObservable() {

    @get:Bindable
    var loading: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    @get:Bindable
    var emailError: Int = Constants.INVALID_RES
        set(value) {
            field = value
            notifyPropertyChanged(BR.emailError)
        }

    @get:Bindable
    var passwordError: Int = Constants.INVALID_RES
        set(value) {
            field = value
            notifyPropertyChanged(BR.passwordError)
        }
}