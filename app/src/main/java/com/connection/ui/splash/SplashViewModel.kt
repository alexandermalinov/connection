package com.connection.ui.splash

import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.SPLASH_SCREEN_DELAY
import com.connection.navigation.NavigationGraph
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashViewModel @Inject constructor() : BaseViewModel() {

    init {
        viewModelScope.launch {
            delay(SPLASH_SCREEN_DELAY)
            _navigationLiveData.value =
                NavigationGraph(R.id.action_splashFragment_to_registerFragment)
        }
    }
}