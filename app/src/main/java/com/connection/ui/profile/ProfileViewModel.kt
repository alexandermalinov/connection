package com.connection.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.profile.ProfileUiModel
import com.connection.vo.profile.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(), ProfilePresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ProfileUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(ProfileUiModel())
    private var loggedUserId = EMPTY

    init {
        viewModelScope.launch {
            initUserData()
            userRepository.getUser(loggedUserId) { user ->
                _uiLiveData.value = user?.toUiModel()
            }
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun initUserData() {
        loggedUserId = userRepository.getLoggedUserId()
    }
}