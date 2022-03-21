package com.connection.ui.connectionchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.UserData
import com.connection.data.repository.chat.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.vo.connectionchat.ConnectionChatUiModel
import com.connection.vo.connectionchat.HeaderUiModel
import com.connection.vo.message.LoggedUserMessageUiModel
import com.connection.vo.message.MessageUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectionChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatRepository: ChatTabRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(), ConnectionChatPresenter {

    val uiLiveData: LiveData<ConnectionChatUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(ConnectionChatUiModel())
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            initUserData()
        }
    }

    private suspend fun initUserData() {
        userRepository.getLoggedUser { user ->
            loggedUser = user
            initHeaderData()
        }
    }

    private fun initHeaderData() {
        savedStateHandle.get<HeaderUiModel>(HEADER_MODEL)?.let { headerModel ->
            _uiLiveData.value = ConnectionChatUiModel(header = headerModel)
        }
    }

    private fun getMessages() = _uiLiveData.value?.messages ?: emptyList()

    private fun createLoggedUserMessage() = LoggedUserMessageUiModel(
        senderPicture = loggedUser?.picture ?: EMPTY,
        senderUsername = loggedUser?.username ?: EMPTY,
        senderOnline = true,
        senderMessage = _uiLiveData.value?.message ?: EMPTY
    )

    private fun createMessage(): List<MessageUiModel> = getMessages()
        .plus(createLoggedUserMessage())

    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSendClick() {
        viewModelScope.launch {
            _uiLiveData.value?.let { model ->
                chatRepository.sendMessage(
                    channelId = model.header.channelId,
                    message = model.message, {
                        _uiLiveData.value = ConnectionChatUiModel(
                            model.header,
                            createMessage()
                        )
                    }, {
                        Timber.e("error occurred sending message...")
                    }
                )
            }
        }
    }
}
