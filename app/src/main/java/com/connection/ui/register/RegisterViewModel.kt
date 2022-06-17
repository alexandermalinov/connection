package com.connection.ui.register

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.remote.model.user.UserData
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.GalleryNavigation
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.isEmailValid
import com.connection.ui.isPasswordValid
import com.connection.ui.isUsernameValid
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.register.RegisterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(), RegisterPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<RegisterUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(RegisterUiModel())

    /* --------------------------------------------------------------------------------------------
     * Exposed
    ---------------------------------------------------------------------------------------------*/
    fun setProfilePicture(uri: Uri) {
        viewModelScope.launch {
            _uiLiveData.value?.profilePicture = uri
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun register() {
        _uiLiveData.value?.let { uiData ->
            userRepository.registerAuth(
                uiData.email,
                uiData.password,
                onFailure = { onFailedValidation() },
                onSuccess = { id -> uploadUserPicture(id, uiData) }
            )
        }
    }

    private fun uploadUserPicture(id: String, uiData: RegisterUiModel) {
        userRepository.uploadImage(
            uiData.profilePicture,
            onFailure = { onFailedValidation() },
            onSuccess = {
                getUser(id, it)?.let { user ->
                    userRepository.registerDB(
                        user = user,
                        onSuccess = { navigate() },
                        onFailure = { onFailedValidation() }
                    )
                }
            }
        )
    }

    private fun getUser(
        id: String?,
        profileImage: Uri?
    ) = _uiLiveData.value?.let {
        UserData(
            id = id ?: EMPTY,
            email = it.email,
            username = it.username,
            password = it.password,
            picture = profileImage.toString()
        )
    }

    private fun navigate() {
        _navigationLiveData.value = NavigationGraph(R.id.action_register_fragment_to_feedFragment)
    }

    private fun isDataValid() = _uiLiveData.value?.email?.isEmailValid() == true &&
            _uiLiveData.value?.password?.isPasswordValid() == true &&
            _uiLiveData.value?.username?.isUsernameValid() == true

    private suspend fun onSuccessfulValidation() {
        _uiLiveData.value?.loading = true
        register()
    }

    private fun onFailedValidation() {
        _uiLiveData.value?.apply {
            loading = false
            if (email.isEmailValid().not())
                emailError = R.string.invalid_email

            if (username.isUsernameValid().not())
                usernameError = R.string.invalid_username

            if (password.isPasswordValid().not())
                passwordError = R.string.invalid_password
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onRegisterClick() {
        viewModelScope.launch {
            if (isDataValid())
                onSuccessfulValidation()
            else
                onFailedValidation()
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
