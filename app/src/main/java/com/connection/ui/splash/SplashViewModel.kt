package com.connection.ui.splash

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.SPLASH_SCREEN_DELAY
import com.connection.navigation.NavigationGraph
import com.connection.utils.common.Constants.USER_ID
import com.google.firebase.auth.FirebaseAuth
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
            delay(SPLASH_SCREEN_DELAY)
            navigateUser()
        }
    }

    private suspend fun navigateUser() {
        _navigationLiveData.value = if (userRepository.isUserLoggedIn())
            NavigationGraph(
                R.id.action_splashFragment_to_allMessagesFragment,
                bundleOf(USER_ID to userRepository.getLoggedUserId())
            )
        else
            NavigationGraph(R.id.action_splashFragment_to_loginFragment)
    }
}