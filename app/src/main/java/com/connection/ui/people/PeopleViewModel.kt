package com.connection.ui.people

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.UserData
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.people.PeopleUiModel
import com.connection.vo.people.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val uiLiveData: LiveData<PeopleUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(PeopleUiModel())
    private var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            initUserData()
            initUiData()
        }
    }

    private suspend fun initUserData() {
        userRepository.getLoggedUser() {
            loggedUser = it
        }
    }

    private suspend fun initUiData() {
        userRepository.getUsers({ users ->
            _uiLiveData.value = PeopleUiModel(
                loggedUser?.picture ?: EMPTY,
                users?.users
                    ?.map { it.toUiModel() }
                    ?: emptyList()
            )
        }, {
            Timber.e("error occurred while settings peoples data")
        })
    }

}