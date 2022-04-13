package com.connection.ui.connectionchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.UserData
import com.connection.data.repository.chatmessage.ChatMessageRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.ui.base.ConnectionStatus
import com.connection.utils.common.Constants.CONNECTION_CHANNEL_LISTENER
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.vo.connectionchat.ConnectionChatUiModel
import com.connection.vo.connectionchat.HeaderUiModel
import com.connection.vo.message.MessageListUiModel
import com.connection.vo.message.MessageUiModel
import com.connection.vo.message.toUiModel
import com.connection.vo.message.toUiModels
import com.sendbird.android.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectionChatViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatMessageRepository: ChatMessageRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(), ConnectionChatPresenter {

    /* --------------------------------------------------------------------------------------------
    * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<ConnectionChatUiModel>
        get() = _uiLiveData

    val messagesLiveData: LiveData<MessageUiModel>
        get() = _messagesLiveData

    private val _uiLiveData = MutableLiveData(ConnectionChatUiModel()).apply {
        savedStateHandle.get<HeaderUiModel>(HEADER_MODEL)?.let { headerModel ->
            value = ConnectionChatUiModel(headerModel)
        }
    }

    private val _messagesLiveData = MutableLiveData(MessageUiModel())
    private var loggedUser: UserData? = null
    private var senderUser: Member? = null
    private var currentChannel: GroupChannel? = null
    private var senderUserId: String = EMPTY

    init {
        viewModelScope.launch {
            setupUsers()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun setupUsers() {
        userRepository.getLoggedUser { user ->
            loggedUser = user
            senderUserId = savedStateHandle.get<HeaderUiModel>(HEADER_MODEL)?.senderId ?: EMPTY
            viewModelScope.launch {
                setupChat()
            }
        }
    }

    private suspend fun setupChat() {
        chatMessageRepository.getChannel(
            _uiLiveData.value?.header?.channelUrl ?: EMPTY, { channel ->
                currentChannel = channel
                senderUser = getSenderMember(channel)
                setConnectionStatus(getLoggedMember(channel), senderUser)
                initChatHistory(channel)
                addOnReceiveEvent()
            }, {
                setConnectionStatus(getLoggedMember(currentChannel), senderUser)
                Timber.e("error getting channel")
            }
        )
    }


    private fun initChatHistory(channel: GroupChannel?) {
        loadingChat(true)
        channel?.let {
            chatMessageRepository.getChannelMessages(
                it, { messages ->
                    setupMessages(messages)
                    loadingChat(false)
                }, {
                    Timber.e("error occurred fetching message history")
                    loadingChat(false)
                })
        }
    }

    private fun setupMessages(messages: List<BaseMessage>) {
        if (messages.isNullOrEmpty())
            Timber.e("chat has no messages yet")
        else
            _messagesLiveData.value = MessageUiModel(messages.toUiModels(loggedUser?.id))
    }

    private fun loadingChat(loading: Boolean) {
        _uiLiveData.value?.loadingChatHistory = loading
    }

    private fun setConnectionStatus(
        loggedUser: Member?,
        senderUser: Member?
    ) {
        _uiLiveData.value?.connectionStatus = if (loggedUser != null && senderUser != null)
            when {
                loggedUser.memberState == Member.MemberState.JOINED && senderUser.memberState == Member.MemberState.JOINED ->
                    ConnectionStatus.CONNECTED
                loggedUser.memberState == Member.MemberState.JOINED && senderUser.memberState == Member.MemberState.INVITED ->
                    ConnectionStatus.INVITE_SENT
                loggedUser.memberState == Member.MemberState.INVITED && senderUser.memberState == Member.MemberState.JOINED ->
                    ConnectionStatus.INVITE_RECEIVED
                else -> ConnectionStatus.NOT_CONNECTED
            }
        else
            ConnectionStatus.NOT_CONNECTED
    }

    private fun getSenderMember(channel: GroupChannel?) =
        channel?.members?.first { it.userId != loggedUser?.id }

    private fun getLoggedMember(channel: GroupChannel?) =
        channel?.members?.first { it.userId == loggedUser?.id }

    private fun getLoadedMessages() = _messagesLiveData.value?.messages ?: emptyList()

    private fun addNewMessage(message: MessageListUiModel) = MessageUiModel(
        listOf(message).plus(getLoadedMessages())
    )

    private fun addOnReceiveEvent() {
        SendBird.addChannelHandler(
            CONNECTION_CHANNEL_LISTENER,
            object : SendBird.ChannelHandler() {
                override fun onMessageReceived(p0: BaseChannel?, baseMessage: BaseMessage?) {
                    baseMessage?.let {
                        _messagesLiveData.value = addNewMessage(it.toUiModel(loggedUser?.id))
                    }
                }
            })
    }

    private fun sendMessage() {
        currentChannel?.let {
            chatMessageRepository.sendMessage(
                channel = it,
                message = _uiLiveData.value?.message ?: EMPTY,
                onSuccess = { userMessage ->
                    _messagesLiveData.value = addNewMessage(userMessage.toUiModel(loggedUser?.id))
                    _uiLiveData.value?.message = EMPTY
                },
                onFailure = {
                    Timber.e("error occurred sending message")
                }
            )
        }
    }

    private suspend fun sendInitialMessage() {
        chatMessageRepository.createChannel(
            usersIds = Pair(loggedUser?.id ?: EMPTY, senderUserId),
            onSuccess = { channel ->
                chatMessageRepository.inviteUser(
                    channel,
                    senderUserId, {
                        _uiLiveData.value?.connectionStatus = ConnectionStatus.INVITE_SENT
                        currentChannel = channel
                        sendMessage()
                    }, {
                        Timber.e("error occurred creating channel")
                    }
                )
            },
            onFailure = {
                Timber.e("error occurred creating channel")
            }
        )
    }

    private fun isInitialMessage() =
        _uiLiveData.value?.connectionStatus == ConnectionStatus.NOT_CONNECTED

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSendClick() {
        viewModelScope.launch {
            if (isInitialMessage())
                sendInitialMessage()
            else
                sendMessage()
        }
    }

    override fun onAcceptClick() {
        viewModelScope.launch {
            currentChannel?.let { channel ->
                chatMessageRepository.acceptInvite(
                    channel = channel,
                    onSuccess = {
                        _uiLiveData.value?.connectionStatus = ConnectionStatus.CONNECTED
                        initChatHistory(channel)
                        userRepository.updateUser(
                            userId = loggedUser?.id ?: EMPTY,
                            connections = loggedUser?.connections
                                ?.plus(Pair(senderUserId, senderUser?.profileUrl ?: EMPTY))
                                ?: emptyMap()
                        )
                    },
                    onFailure = {
                        Timber.e("error occurred accepting invite")
                    }
                )
            }
        }
    }

    override fun onDeclineClick() {
        viewModelScope.launch {
            currentChannel?.let { channel ->
                chatMessageRepository.declineInvite(
                    channel = channel,
                    onSuccess = {
                        _navigationLiveData.value = PopBackStack
                    },
                    onFailure = {
                        Timber.e("error occurred declining invite")
                    }
                )
            }
        }
    }
}
