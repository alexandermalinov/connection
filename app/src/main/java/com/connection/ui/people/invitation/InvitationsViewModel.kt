package com.connection.ui.people.invitation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.people.base.PeopleViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.people.PeopleUiModel
import com.connection.vo.people.toUiModels
import com.sendbird.android.GroupChannel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class InvitationsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository
) : PeopleViewModel(userRepository) {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<PeopleUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(PeopleUiModel())

    init {
        viewModelScope.launch {
            setupPeople()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun setupPeople() {
        _uiLiveData.value?.apply {
            loading = true
            chatTabRepository.fetchChannels(
                ConnectionStatus.INVITE_RECEIVED,
                onSuccess = { channels ->
                    onChannelsObtained(channels)
                    loading = false
                },
                onFailure = {
                    Timber.e("Failed to fetch channels")
                    loading = false
                })
        }
    }

    private fun onChannelsObtained(channels: List<GroupChannel>) {
        channels
            .filter { channel -> channel.joinedMemberCount == 1 }
            .toUiModels(loggedUser?.id ?: EMPTY, ConnectionStatus.INVITE_RECEIVED)
            .let {
                if (it.isNullOrEmpty().not())
                    _uiLiveData.value = PeopleUiModel(it)
                else
                    _uiLiveData.value?.emptyState = true
            }
    }
}