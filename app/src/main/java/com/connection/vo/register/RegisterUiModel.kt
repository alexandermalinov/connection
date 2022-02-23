package com.connection.vo.register

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import java.util.*

data class RegisterUiModel(
    val id: UUID,
    val profilePicture: String
) : BaseObservable() {

    @get:Bindable
    var email: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.email)
        }

    @get:Bindable
    var username: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.username)
        }

    @get:Bindable
    var password: String = EMPTY
        set(value) {
            field = value
            notifyPropertyChanged(BR.password)
        }
}