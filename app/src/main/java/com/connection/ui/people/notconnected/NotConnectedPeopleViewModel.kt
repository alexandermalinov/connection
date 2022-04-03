package com.connection.ui.people.notconnected

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.ui.people.base.PeopleViewModel
import com.connection.vo.people.PeopleUiModel
import com.connection.vo.people.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotConnectedPeopleViewModel @Inject constructor(
    userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository
) : PeopleViewModel(userRepository, chatTabRepository) {

    val uiLiveData: LiveData<PeopleUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(PeopleUiModel())

    init {
        viewModelScope.launch {
            initUiData()
        }
    }

    private suspend fun initUiData() {
        _uiLiveData.value?.loading = true
        chatTabRepository.fetchMembers(false, { users ->
            _uiLiveData.value = PeopleUiModel(users.toUiModels())
        }, {
            _uiLiveData.value?.loading = false
            Timber.e("error occurred while settings peoples data")
        })
    }
}