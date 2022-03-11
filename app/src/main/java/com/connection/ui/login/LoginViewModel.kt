package com.connection.ui.login

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
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
                    // TODO
                    _uiLiveData.value?.loading = true
                    onLoginNavigate(id)
                },
                onFailure = {
                    // TODO
                    _uiLiveData.value?.emailError = R.string.invalid_email
                    _uiLiveData.value?.passwordError = R.string.invalid_password
                    _uiLiveData.value?.loading = false
                })
        }
    }

    private fun onLoginNavigate(id: String?) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_loginFragment_to_allMessagesFragment,
            bundleOf(USER_ID to id)
        )
    }

    override fun onLoginClick() {
        _uiLiveData.value?.let {
            login(it.email, it.password)
        }
    }

    override fun onRegisterClick() {
        _navigationLiveData.value = NavigationGraph(R.id.action_loginFragment_to_registerFragment)
    }
}