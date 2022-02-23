package com.connection.ui.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.connection.R
import com.connection.navigation.NavigationGraph
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.AUTH_KEY
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.register.RegisterUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor() : BaseViewModel(), RegisterPresenter {

    val uiLiveData: LiveData<RegisterUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(RegisterUiModel(UUID.randomUUID(), EMPTY))

    override fun onRegisterClick() {
        val user = User(_uiLiveData.value?.id.toString(), _uiLiveData.value?.username)

        CometChat.createUser(
            user,
            AUTH_KEY,
            object : CometChat.CallbackListener<User>() {
                override fun onSuccess(p0: User?) {
                    _navigationLiveData.value =
                        NavigationGraph(R.id.action_registerFragment_to_allMessagesFragment)
                    Timber.e("Successful registration user: $p0")
                }

                override fun onError(p0: CometChatException?) {
                    Timber.e("Error occurred on registering user: $p0")
                }

            })
    }
}