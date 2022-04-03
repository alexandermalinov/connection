package com.connection.ui.connectionchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.USER_EXTRA_DATA_PICTURE
import com.connection.vo.connectionchat.ConnectionChatUiModel
import com.connection.vo.connectionchat.HeaderUiModel
import com.connection.vo.message.*
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

    val messagesLiveData: LiveData<MessageUiModel>
        get() = _messagesLiveData

    private val _uiLiveData = MutableLiveData(ConnectionChatUiModel())
    private val _messagesLiveData = MutableLiveData(MessageUiModel())
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
            chatRepository.getChannel(headerModel.channelId, { channel ->
                currentChannel = channel
                senderUser = channel.getSenderUser()
                _uiLiveData.value = ConnectionChatUiModel(headerModel)
                initChatHistory(channel)
                addOnReceiveEvent()
            }, {
            })
        }
    }

    private fun initChatHistory(channel: Channel?) {
        _uiLiveData.value?.loadingChatHistory = true
        _messagesLiveData.value = MessageUiModel(getChatHistory(channel?.messages ?: emptyList()))
        _uiLiveData.value?.loadingChatHistory = false
    }

    private fun Channel.getSenderUser() = members.first { it.user.id != loggedUser?.id }

    private fun getChatHistory(messages: List<Message>) = messages
        .toUiModel(loggedUser?.id ?: EMPTY)
        .reversed()

    private fun getOldMessages() = _messagesLiveData.value?.messages ?: emptyList()

    private fun getRightNewMessage(message: String) = LoggedUserMessageUiModel().apply {
        senderPicture = loggedUser?.picture ?: EMPTY
        senderMessage = message
    }

    private fun getLeftNewMessage(message: String) = SenderUserMessageUiModel().apply {
        senderPicture = senderUser?.user?.extraData
            ?.get(USER_EXTRA_DATA_PICTURE)
            ?.toString()
            ?: EMPTY
        senderMessage = message
    }

    private fun addNewMessage(message: MessageListUiModel) = listOf(message).plus(getOldMessages())

    private fun isRightMessage(message: Message) = message.user.id == loggedUser?.id

    private fun addOnReceiveEvent() {
        client
            .channel(currentChannel?.cid ?: EMPTY)
            .subscribe { event ->
                when (event) {
                    is NewMessageEvent -> {
                        onNewMessage(event.message)
                    }
                }
            }
    }

    private fun onNewMessage(message: Message) {
        if (isRightMessage(message))
            _messagesLiveData.value =
                MessageUiModel(addNewMessage(getRightNewMessage(message.text)))
        else
            _messagesLiveData.value = MessageUiModel(addNewMessage(getLeftNewMessage(message.text)))
    }

    private suspend fun sendMessage() {
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

    private suspend fun sendInitialMessage() {
        /*chatRepository.createChannel(
            listOf(loggedUser?.id ?: EMPTY),
            mapOf(CHANNEL_INVITES to senderId), { channel ->
                viewModelScope.launch {
                    currentChannel = channel
                    _uiLiveData.value?.connectionStatus = ConnectionStatus.PENDING
                }
            }, {
                Timber.e("error occurred inviting member")
            }
        )*/
    }

    private fun isInitialMessage() =
        _uiLiveData.value?.connectionStatus == ConnectionStatus.NOT_CONNECTED

    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSendClick() {
        viewModelScope.launch {
            _uiLiveData.value?.let { model ->
                /* if (isInitialMessage())
                     sendInitialMessage()
                 else*/
                sendMessage()
                model.message = EMPTY
            }
        }
    }

    override fun onAcceptClick() {
        viewModelScope.launch {
            chatRepository.acceptInvite(
                currentChannel?.id ?: EMPTY,
                _uiLiveData.value?.message ?: EMPTY, {
                    _uiLiveData.value?.connectionStatus = ConnectionStatus.CONNECTED
                }, {
                    _uiLiveData.value?.connectionStatus = ConnectionStatus.PENDING
                }
            )
        }
    }

    override fun onDeclineClick() {
        viewModelScope.launch {
            chatRepository.declineInvite(
                currentChannel?.id ?: EMPTY, { channel ->
                    currentChannel = channel
                    _uiLiveData.value?.connectionStatus = ConnectionStatus.NOT_CONNECTED
                }, {
                    Timber.e("error declining invitation")
                }
            )
        }
    }
}
