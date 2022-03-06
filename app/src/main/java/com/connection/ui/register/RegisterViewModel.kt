package com.connection.ui.register

import android.net.Uri
import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.User
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.GalleryNavigation
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.isEmailValid
import com.connection.ui.isPasswordValid
import com.connection.ui.isUsernameValid
import com.connection.utils.common.Constants.INVALID_RES
import com.connection.utils.common.Constants.USER_ID
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

    fun setProfilePicture(uri: Uri) {
        viewModelScope.launch {
            _uiLiveData.value?.profilePicture = uri
        }
    }

    private suspend fun register() {
        _uiLiveData.value?.let { uiData ->
            userRepository.apply {
                registerAuth(uiData.email, uiData.password) {
                    viewModelScope.launch {
                        uploadImage(uiData.profilePicture) {
                            initUser(it)?.let { user ->
                                viewModelScope.launch {
                                    registerDB(user) { registeredUser ->
                                        onRegister(registeredUser)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initUser(profileImage: Uri?) = _uiLiveData.value?.let {
        User(
            email = it.email,
            username = it.username,
            password = it.password,
            picture = profileImage.toString()
        )
    }

    private fun onRegister(user: User) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_registerFragment_to_allMessagesFragment,
            bundleOf(USER_ID to user.id)
        )
    }

    private fun isDataValid() = _uiLiveData.value?.email?.isEmailValid() == true &&
            _uiLiveData.value?.password?.isPasswordValid() == true &&
            _uiLiveData.value?.username?.isUsernameValid() == true

    private suspend fun onSuccessfulValidation() {
        _uiLiveData.value?.loading = true
        register()
    }

    private fun onFailureValidation() {
        _uiLiveData.value?.apply {
            loading = false
            emailError = if (email.isEmailValid().not())
                R.string.invalid_email
            else
                INVALID_RES

            usernameError = if (username.isUsernameValid().not())
                R.string.invalid_username
            else
                INVALID_RES

            passwordError = if (password.isPasswordValid().not())
                R.string.invalid_password
            else
                INVALID_RES
        }
    }

    override fun onRegisterClick() {
        viewModelScope.launch {
            if (isDataValid())
                onSuccessfulValidation()
            else
                onFailureValidation()
        }
    }

    override fun onLoginClick() {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_registerFragment_to_loginFragment
        )
    }

    override fun onProfileImageClick() {
        _navigationLiveData.value = GalleryNavigation
    }
}
