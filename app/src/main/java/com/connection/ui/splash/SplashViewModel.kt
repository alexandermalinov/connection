package com.connection.ui.splash

import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.SPLASH_SCREEN_DELAY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            navigateUser()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun navigateUser() {
        if (userRepository.isUserLoggedIn()) {
            delay(SPLASH_SCREEN_DELAY)
            navigateToFeed()
        } else {
            delay(SPLASH_SCREEN_DELAY)
            navigateToLogin()
        }
    }

    private fun navigateToFeed() {
        _navigationLiveData.value = NavigationGraph(R.id.action_splash_fragment_to_feedFragment)
    }

    private fun navigateToLogin() {
        _navigationLiveData.value = NavigationGraph(R.id.action_splashFragment_to_loginFragment)
    }
}