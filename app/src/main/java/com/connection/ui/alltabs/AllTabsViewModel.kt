package com.connection.ui.alltabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.ChannelExtraData
import com.connection.data.api.model.UserData
import com.connection.data.api.model.toUiModel
import com.connection.data.repository.chat.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.CHANNEL_TYPE_MESSAGING
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.USER_ID
import com.connection.vo.alltabs.AllTabsUiModel
import com.connection.vo.connectiontab.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AllTabsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), AllTabsPresenter {

    val uiLiveData: LiveData<AllTabsUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(AllTabsUiModel())
    private val loggedUserId = savedStateHandle.get<String>(USER_ID) ?: EMPTY
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            initData()
        }
    }

    private suspend fun initData() {
        userRepository.getUser(loggedUserId) { user ->
            viewModelScope.launch {
                loggedUser = user
                initUserData()
                fetchChannels()
            }
        }
    }

    private fun initUserData() {
        loggedUser?.toUiModel()?.let {
            chatTabRepository.connectUser(
                it, {
                    _uiLiveData.value = AllTabsUiModel(
                        loggedUser?.picture ?: EMPTY,
                        emptyList()
                    )
                }, {
                    Timber.e("error occurred while trying to connect user")
                }
            )
        }
    }

    private suspend fun fetchChannels() {
        chatTabRepository.fetchChannels({ channels ->
            _uiLiveData.value = AllTabsUiModel(
                profileImage = _uiLiveData.value?.profileImage ?: EMPTY,
                connections = channels.toUiModels()
            )
        }, {
            Timber.e("Failed to fetch channels")
        })
    }

    private suspend fun createChannel() {
        chatTabRepository.createChannelByIds(
            CHANNEL_TYPE_MESSAGING,
            listOf(
                loggedUserId,
                "cQPglspnyYOJuFsCy0HF8qyijvm1"
            ),
            ChannelExtraData(
                "victoria",
                "https://firebasestorage.googleapis.com/v0/b/connection-18947.appspot.com/o/profile_image%2F7b5807c7-35a1-42d7-b475-3102540845f9?alt=media&token=0531dfee-25aa-4968-a126-e5ca72adec11"
            ), {
                Timber.e("Successfully created channel")
            }, {
                Timber.e("Failed to create channel")
            }
        )
    }

    override fun onSearchClick() {
        viewModelScope.launch {
            createChannel()
        }
    }
}