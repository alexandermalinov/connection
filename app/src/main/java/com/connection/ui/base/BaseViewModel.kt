package com.connection.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.connection.utils.SingleLiveEvent
import com.connection.navigation.Destination

open class BaseViewModel : ViewModel() {

    val navigationLiveData: LiveData<Destination>
        get() = _navigationLiveData

    protected val _navigationLiveData = SingleLiveEvent<Destination>()
}