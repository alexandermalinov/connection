package com.connection.ui.splash

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.user.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.SPLASH_SCREEN_DELAY
import com.connection.utils.common.Constants.USER_ID
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository
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
            onLoggedUser()
        } else {
            delay(SPLASH_SCREEN_DELAY)
            navigateToLogin()
        }
    }

    private suspend fun onLoggedUser() {
        userRepository.getLoggedUser { user ->
            connectUser(user)
        }
    }

    private fun connectUser(user: UserData?) {
        user?.let {
            chatTabRepository.connectUser(
                it, {
                    navigateToFeed(user)
                }, {
                    Timber.e("error occurred while trying to connect user")
                }
            )
        }
    }

    private fun navigateToFeed(user: UserData?) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_splash_fragment_to_feedFragment,
            bundleOf(USER_ID to user?.id)
        )
    }

    private fun navigateToLogin() {
        _navigationLiveData.value = NavigationGraph(R.id.action_splashFragment_to_loginFragment)
    }
}