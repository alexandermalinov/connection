package com.connection.ui.login

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.isEmailValid
import com.connection.ui.isPasswordValid
import com.connection.ui.isUsernameValid
import com.connection.utils.common.Constants
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.login.LoginUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(), LoginPresenter {

    val uiLiveData: LiveData<LoginUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(LoginUiModel())

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            userRepository.login(
                email = email,
                password = password,
                onSuccess = { id ->
                    _uiLiveData.value?.loading = true
                    onLoginNavigate(id)
                },
                onFailure = {
                    onFailureValidation()
                })
        }
    }

    private fun onFailureValidation() {
        _uiLiveData.value?.apply {
            loading = false
            emailError = if (email.isEmailValid().not())
                R.string.invalid_email
            else
                Constants.INVALID_RES

            passwordError = if (password.isPasswordValid().not())
                R.string.invalid_password
            else
                Constants.INVALID_RES
        }
    }

    private fun isDataValid() = _uiLiveData.value?.email?.isEmailValid() == true &&
            _uiLiveData.value?.password?.isPasswordValid() == true

    private fun onLoginNavigate(id: String?) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_loginFragment_to_allMessagesFragment,
            bundleOf(USER_ID to id)
        )
    }

    override fun onLoginClick() {
        _uiLiveData.value?.let {
            if (isDataValid()) {
                login(it.email, it.password)
            } else {
                onFailureValidation()
            }
        }
    }

    override fun onRegisterClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_loginFragment_to_registerFragment)
    }
}