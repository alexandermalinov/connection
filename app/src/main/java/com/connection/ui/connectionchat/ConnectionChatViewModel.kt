package com.connection.ui.connectionchat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.connection.data.repository.chat.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.PopBackStack
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.vo.connectionchat.ConnectionChatUiModel
import com.connection.vo.connectionchat.HeaderUiModel
import com.connection.vo.connectiontab.ConnectionTabUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ConnectionChatViewModel @Inject constructor(
    private val chatRepository: ChatTabRepository,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel(), ConnectionChatPresenter {

    val uiLiveData: LiveData<ConnectionChatUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(ConnectionChatUiModel())

    init {
        initUsersData()
    }

    private fun initUsersData() {
        savedStateHandle.get<HeaderUiModel>(HEADER_MODEL)?.let { headerModel ->
            _uiLiveData.value = ConnectionChatUiModel(
                header = headerModel,

            )
        }
    }

    override fun onBackClick() {
        _navigationLiveData.value = PopBackStack
    }

    override fun onSendClick() {
        // TODO("Not yet implemented")
    }
}
