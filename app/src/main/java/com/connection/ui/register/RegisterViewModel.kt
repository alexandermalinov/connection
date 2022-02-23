package com.connection.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.connection.R
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.ui.isEmailValid
import com.connection.utils.common.Constants.AUTH_KEY
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.register.RegisterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.serialization.json.Json
import org.json.JSONObject
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : BaseViewModel(), RegisterPresenter {

    val uiLiveData: LiveData<RegisterUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(RegisterUiModel())

    private fun getUser(): User = User(
        _uiLiveData.value?.id.toString(),
        _uiLiveData.value?.username
    ).also {
        it.metadata = JSONObject(
            mapOf(
                "email" to _uiLiveData.value?.email,
                "password" to _uiLiveData.value?.password
            )
        )
    }

    override fun onRegisterClick() {
        if (_uiLiveData.value?.email?.isEmailValid() == true) {
            CometChat.createUser(
                getUser(),
                AUTH_KEY,
                object : CometChat.CallbackListener<User>() {
                    override fun onSuccess(p0: User?) {
                        _uiLiveData.value?.loading = true
                        _navigationLiveData.value =
                            NavigationGraph(R.id.action_registerFragment_to_allMessagesFragment)
                        Timber.e("Successful registration user: $p0")
                    }

                    override fun onError(p0: CometChatException?) {
                        _uiLiveData.value?.loading = false
                        Timber.e("Error occurred on registering user: $p0")
                    }

                })
        } else {
            _uiLiveData.value?.loading = false
        }
    }
}