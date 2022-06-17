package com.connection.vo.people

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR

open class PeopleUiModel : BaseObservable() {

    @get:Bindable
    var loading = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }

    @get:Bindable
    var emptyState = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.emptyState)
        }
}