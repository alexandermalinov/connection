package com.connection.vo.register

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import java.util.*

data class RegisterUiModel(
    val id: UUID = UUID.randomUUID(),
    var email: String = EMPTY,
    var username: String = EMPTY,
    var password: String = EMPTY,
    val profilePicture: String = EMPTY
) : BaseObservable() {

    @get:Bindable
    var loading: Boolean = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }
}