package com.connection.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.connection.ui.base.BaseViewModel
import com.connection.vo.register.RegisterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : BaseViewModel(), RegisterPresenter {

    val uiLiveData: LiveData<RegisterUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(RegisterUiModel())

    override fun onRegisterClick() {
        // TODO
    }
}