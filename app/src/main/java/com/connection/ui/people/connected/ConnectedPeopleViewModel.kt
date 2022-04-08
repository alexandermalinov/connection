package com.connection.ui.people.connected

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.api.model.UsersData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.ui.people.base.PeopleViewModel
import com.connection.utils.common.Constants
import com.connection.vo.people.PeopleUiModel
import com.connection.vo.people.toUiModels
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ConnectedPeopleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    chatTabRepository: ChatTabRepository
) : PeopleViewModel(userRepository, chatTabRepository) {

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
            userRepository.getUsers({ users ->
                onPeopleObtained(users)
                loading = false
            }, {
                loading = false
                Timber.e("error occurred while settings peoples data")
            })
        }
    }

    private fun onPeopleObtained(usersData: UsersData) {
        filterConnectedPeople(usersData).let { people ->
            if (people.isNullOrEmpty().not())
                _uiLiveData.value = PeopleUiModel(people)
            else
                _uiLiveData.value?.emptyState = true
        }
    }

    private fun filterConnectedPeople(usersData: UsersData) = usersData.users
        .filter { loggedUser?.connections?.contains(it.id) == true }
        .toUiModels(true)

    // TODO ("implement")
    private fun isConnection(user: UserData) =
        loggedUser?.connections?.contains(user.id) == true && loggedUser?.id ?: Constants.EMPTY == user.id
}