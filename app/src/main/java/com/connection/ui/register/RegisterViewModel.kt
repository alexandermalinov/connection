package com.connection.ui.register

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.User
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.register.RegisterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(), RegisterPresenter {

    val uiLiveData: LiveData<RegisterUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(RegisterUiModel())

    private suspend fun register() {
        _uiLiveData.value?.let { uiData ->
            userRepository.registerAuth(uiData.email, uiData.password) {
                viewModelScope.launch {
                    initUser()?.let {
                        userRepository.registerDB(it) { registeredUser ->
                            onRegister(registeredUser)
                        }
                    }
                }
            }
        }
    }

    private fun initUser() = _uiLiveData.value?.let {
        User(
            email = it.email,
            username = it.username,
            password = it.password,
            profilePicture = EMPTY
        )
    }

    private fun onRegister(user: User) {
        _uiLiveData.value?.loading = true
        _navigationLiveData.value = NavigationGraph(
            R.id.action_registerFragment_to_allMessagesFragment,
            bundleOf(
                "id" to user.id,
                "username" to user.username,
                "user_password" to user.profilePicture
            )
        )
    }

    override fun onRegisterClick() {
        viewModelScope.launch {
            register()
        }
    }
}