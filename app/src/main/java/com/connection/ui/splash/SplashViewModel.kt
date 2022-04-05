package com.connection.ui.splash

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.SPLASH_SCREEN_DELAY
import com.connection.navigation.NavigationGraph
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

    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            navigateUser()
        }
    }

    private suspend fun navigateUser() {
        _navigationLiveData.value = if (userRepository.isUserLoggedIn()) {
            initData()
            delay(SPLASH_SCREEN_DELAY)
            createNavigationToAllTabs()
        } else {
            delay(SPLASH_SCREEN_DELAY)
            NavigationGraph(R.id.action_splashFragment_to_loginFragment)
        }
    }

    private suspend fun initData() {
        userRepository.getLoggedUser { user ->
            loggedUser = user
            connectUser()
        }
    }

    private fun connectUser() {
        loggedUser?.let {
            chatTabRepository.connectUser(
                it, {
                    Timber.e("logged user is connected")
                }, {
                    Timber.e("error occurred while trying to connect user")
                }
            )
        }
    }

    private suspend fun createNavigationToAllTabs() = NavigationGraph(
        R.id.action_splashFragment_to_allMessagesFragment,
        bundleOf(USER_ID to userRepository.getLoggedUserId())
    )
}