package com.connection.ui.people.connected

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
class ConnectedPeopleViewModel @Inject constructor(
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
                ConnectionStatus.CONNECTED,
                onSuccess = { channels ->
                    onInvitationsObtained(channels)
                    loading = false
                },
                onFailure = {
                    Timber.e("Failed to fetch channels")
                    loading = false
                })
        }
    }

    private fun onInvitationsObtained(channels: List<GroupChannel>) {
        channels
            .toUiModels(loggedUser?.id ?: EMPTY, ConnectionStatus.CONNECTED)
            .let {
                if (it.isNullOrEmpty().not())
                    _uiLiveData.value = PeopleUiModel(it)
                else
                    _uiLiveData.value?.emptyState = true
            }
    }
}