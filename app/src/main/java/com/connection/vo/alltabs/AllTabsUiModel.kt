package com.connection.vo.alltabs

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.connection.BR
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.connectiontab.ConnectionTabUiModel

data class AllTabsUiModel(
    val profileImage: String = EMPTY
) : BaseObservable() {

    @get:Bindable
    var loadingFavourites = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingFavourites)
        }

    @get:Bindable
    var loadingConnections = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.loadingConnections)
        }

    @get:Bindable
    var emptyChannelList = false
        set(value) {
            field = value
            notifyPropertyChanged(BR.emptyChannelList)
        }
}