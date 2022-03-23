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
import com.connection.utils.common.Constants.EXTRA_DATA_PICTURE
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.vo.connectionchat.ConnectionChatUiModel
import com.connection.vo.connectionchat.HeaderUiModel
import com.connection.vo.message.LoggedUserMessageUiModel
import com.connection.vo.message.MessageUiModel
import com.connection.vo.message.SenderUserMessageUiModel
import com.connection.vo.message.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.events.NewMessageEvent
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Member
import io.getstream.chat.android.client.models.Message
import io.getstream.chat.android.client.models.User
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectionChatViewModel @Inject constructor(
    private val client: ChatClient,
    private val userRepository: UserRepository,
    private val chatRepository: ChatTabRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(), ConnectionChatPresenter {

    val uiLiveData: LiveData<ConnectionChatUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(ConnectionChatUiModel())
    private var loggedUser: UserData? = null
    private var senderUser: Member? = null
    private var currentChannel: Channel? = null

    init {
        viewModelScope.launch {
            initUserData()
            initChatData()
        }
    }

    private suspend fun initUserData() {
        userRepository.getLoggedUser { user ->
            loggedUser = user
        }
    }

    private suspend fun initChatData() {
        savedStateHandle.get<HeaderUiModel>(HEADER_MODEL)?.let { headerModel ->
            chatRepository.getChannel(headerModel.channelId) { channel ->
                currentChannel = channel
                senderUser = channel.getSenderUser()
                _uiLiveData.value = ConnectionChatUiModel(
                    headerModel,
                    getChatHistory(channel.messages)
                )
                addOnReceiveEvent()
            }
        }
    }

    private fun Channel.getSenderUser() = members.first { it.user.id != loggedUser?.id }

    private fun getChatHistory(messages: List<Message>) = messages
        .toUiModel(loggedUser?.id ?: EMPTY)
        .reversed()

    private fun getOldMessages() = _uiLiveData.value?.messages ?: emptyList()

    private fun getRightNewMessage(message: String) = LoggedUserMessageUiModel().apply {
        senderPicture = loggedUser?.picture ?: EMPTY
        senderMessage = message
    }

    private fun getLeftNewMessage(message: String) = SenderUserMessageUiModel().apply {
        senderPicture = senderUser?.user
            ?.extraData
            ?.get(EXTRA_DATA_PICTURE)
            ?.toString()
            ?: EMPTY
        senderMessage = message
    }

    private fun addNewMessage(message: MessageUiModel) =
        listOf(message).plus(getOldMessages())

    private fun isRightMessage(message: Message) = message.user.id == loggedUser?.id

    private fun addOnReceiveEvent() {
        _uiLiveData.value?.let { model ->
            client.channel(currentChannel?.cid ?: EMPTY)
                .subscribe { event ->
                    when (event) {
                        is NewMessageEvent -> {
                            onNewMessage(event.message)
                        }
                    }
                }
        }
    }

    private fun onNewMessage(message: Message) {
        _uiLiveData.value?.header?.let { model ->
            _uiLiveData.value = ConnectionChatUiModel(
                model,
                addNewMessage(
                    if (isRightMessage(message))
                        getRightNewMessage(message.text)
                    else
                        getLeftNewMessage(message.text)
                )
            )
        }
    }

    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSendClick() {
        viewModelScope.launch {
            _uiLiveData.value?.let { model ->
                chatRepository.sendMessage(
                    channelId = model.header.channelId,
                    message = Message(
                        text = model.message,
                        user = client.getCurrentUser() ?: User()
                    ), {
                    }, {
                        Timber.e("error occurred sending message...")
                    }
                )
            }
        }
    }
}
