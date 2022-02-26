package com.connection.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.response.User
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

    private fun login(user: User?) {
        viewModelScope.launch {
            userRepository.logUser(user) {
                _navigationLiveData.value = NavigationGraph(
                    R.id.action_registerFragment_to_allMessagesFragment
                )
            }
        }
    }

    private suspend fun register() {
        _uiLiveData.value?.apply {
            userRepository.registerUser(
                User(email, password, username, EMPTY)
            ) { user ->
                login(user)
            }
        }
    }

    override fun onRegisterClick() {
        viewModelScope.launch {
            register()
        }
    }
}