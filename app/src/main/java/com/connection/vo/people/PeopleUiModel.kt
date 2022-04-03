package com.connection.vo.people

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR

data class PeopleUiModel(
    val peoples: List<PeopleListItemUiModel> = emptyList()
) : BaseObservable() {

    @get:Bindable
    var loading = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loading)
        }
}