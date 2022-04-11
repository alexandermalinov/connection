package com.connection.ui.people.notconnected

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.UserData
import com.connection.data.api.model.UsersData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.ConnectionStatus
import com.connection.ui.people.base.PeopleViewModel
import com.connection.vo.people.PeopleUiModel
import com.connection.vo.people.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotConnectedPeopleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    chatTabRepository: ChatTabRepository
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
            userRepository.getUsers(
                onSuccess = { users ->
                    onPeopleObtained(users)
                    loading = false
                },
                onFailure = {
                    loading = false
                    Timber.e("error occurred while settings peoples data")
                })
        }
    }

    private fun onPeopleObtained(usersData: UsersData) {
        filterNotConnectedPeople(usersData).let { people ->
            if (people.isNullOrEmpty().not())
                _uiLiveData.value = PeopleUiModel(people)
            else
                _uiLiveData.value?.emptyState = true
        }
    }

    private fun filterNotConnectedPeople(usersData: UsersData) = usersData.users
        .filterNot { user -> isConnected(user)  && user.id == loggedUser?.id}
        .toUiModels(ConnectionStatus.NOT_CONNECTED)

    private fun isConnected(user: UserData) = loggedUser?.connections?.contains(user.id) == true
}
