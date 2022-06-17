package com.connection.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.isEmailValid
import com.connection.ui.isPasswordValid
import com.connection.vo.login.LoginUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(), LoginPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<LoginUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(LoginUiModel())

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun login(email: String, password: String) {
        _uiLiveData.value?.loading = true
        userRepository.login(
            email = email,
            password = password,
            onSuccess = { navigateHome() },
            onFailure = { showError() }
        )
    }

    private fun showError() {
        _uiLiveData.value?.apply {
            loading = false
            if (email.isEmailValid().not())
                emailError = R.string.invalid_email

            if (password.isPasswordValid().not())
                passwordError = R.string.invalid_password
        }
    }

    private fun isDataValid() = _uiLiveData.value?.email?.isEmailValid() == true &&
            _uiLiveData.value?.password?.isPasswordValid() == true

    private fun navigateHome() {
        _navigationLiveData.value = NavigationGraph(R.id.action_login_fragment_to_feedFragment)
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onLoginClick() {
        viewModelScope.launch {
            _uiLiveData.value?.let {
                if (isDataValid()) {
                    login(it.email, it.password)
                } else {
                    showError()
                }
            }
        }
    }

    override fun onRegisterClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_loginFragment_to_registerFragment)
    }
}