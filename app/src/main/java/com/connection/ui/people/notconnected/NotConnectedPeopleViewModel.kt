package com.connection.ui.people.notconnected

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.remote.model.user.UserData
import com.connection.data.api.remote.model.user.UsersData
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.people.base.PeopleViewModel
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.vo.people.notconnected.NotConnectedPeopleListItemUiModel
import com.connection.vo.people.notconnected.NotConnectedUiModel
import com.connection.vo.people.notconnected.toUiModel
import com.connection.vo.people.notconnected.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class NotConnectedPeopleViewModel @Inject constructor(
    private val userRepository: UserRepository
) : PeopleViewModel(userRepository), NotConnectedPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val uiLiveData: LiveData<NotConnectedUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(NotConnectedUiModel())

    init {
        viewModelScope.launch {
            loadPeople()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun loadPeople() {
        _uiLiveData.value?.loading = true
        userRepository.getUsers(
            onSuccess = { users -> setUiData(users) },
            onFailure = { Timber.e("error occurred while settings peoples data") })
        _uiLiveData.value?.loading = false
    }

    private fun setUiData(usersData: UsersData) {
        filterNotConnectedPeople(usersData).let { people ->
            if (people.isNotEmpty())
                _uiLiveData.value = NotConnectedUiModel(people)
            else
                _uiLiveData.value?.emptyState = true
        }
    }

    private fun filterNotConnectedPeople(usersData: UsersData) = usersData.users
        .filter { user -> isConnected(user).not() && user.id != loggedUser?.id }
        .toUiModels()

    private fun isConnected(user: UserData) = loggedUser?.connections?.keys
        ?.any { it == user.id } == true

    private fun navigateToChat(senderUser: NotConnectedPeopleListItemUiModel) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_peopleFragment_to_connectionChatFragment,
            bundleOf(HEADER_MODEL to senderUser.toUiModel())
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onConnectClick(user: NotConnectedPeopleListItemUiModel) {
        navigateToChat(user)
    }
}
