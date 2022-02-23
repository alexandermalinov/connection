package com.connection.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cometchat.pro.core.CometChat
import com.connection.ui.base.BaseViewModel
import com.connection.vo.login.LoginUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : BaseViewModel(), LoginPresenter {

    val uiLiveData: LiveData<LoginUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(LoginUiModel())

    override fun onLoginClick() {
        //CometChat.login()
    }
}