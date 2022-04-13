package com.connection.ui.people.notconnected

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.api.model.UsersData
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.people.base.PeopleViewModel
import com.connection.utils.common.Constants
import com.connection.vo.people.PeopleListItemUiModel
import com.connection.vo.people.notconnected.NotConnectedPeopleListItemUiModel
import com.connection.vo.people.notconnected.NotConnectedUiModel
import com.connection.vo.people.notconnected.toUiModel
import com.connection.vo.people.notconnected.toUiModels
import com.connection.vo.people.toUiModel
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
                _uiLiveData.value = NotConnectedUiModel(people)
            else
                _uiLiveData.value?.emptyState = true
        }
    }

    private fun filterNotConnectedPeople(usersData: UsersData) = usersData.users
        .filterNot { user -> isConnected(user) && user.id == loggedUser?.id }
        .toUiModels()

    private fun isConnected(user: UserData) = loggedUser?.connections?.contains(user.id) == true

    private fun navigateToChat(senderUser: NotConnectedPeopleListItemUiModel) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_peopleFragment_to_connectionChatFragment,
            bundleOf(Constants.HEADER_MODEL to senderUser.toUiModel())
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onSendRequestClick(user: NotConnectedPeopleListItemUiModel) {
        navigateToChat(user)
    }
}
