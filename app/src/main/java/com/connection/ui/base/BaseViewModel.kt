package com.connection.ui.base

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.connection.R
import com.connection.utils.SingleLiveEvent
import com.connection.navigation.Destination
import com.connection.navigation.NavigationGraph
import com.connection.utils.common.Constants

open class BaseViewModel : ViewModel() {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val navigationLiveData: LiveData<Destination>
        get() = _navigationLiveData

    protected val _navigationLiveData = SingleLiveEvent<Destination>()
}