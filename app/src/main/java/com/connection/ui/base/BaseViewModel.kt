package com.connection.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.connection.utils.navigation.Destination

class BaseViewModel : ViewModel() {

    val navigationLiveData: LiveData<Destination>
        get() = _navigationLiveData

    protected _navigationLiveData = SingleLiveEvent<Destination>()
}