package com.connection.ui.alltabs

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.connection.data.repository.user.UserRepository
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.EMPTY
import com.connection.vo.alltabs.AllTabsUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AllTabsViewModel @Inject constructor(
    private val userRepository: UserRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(), AllTabsPresenter {

    val uiLiveData: LiveData<AllTabsUiModel>
        get() = _uiLiveData

    private val _uiLiveData = MutableLiveData(AllTabsUiModel())
    private val loggedUserId = userRepository.getLoggedUserId()//savedStateHandle.get<String>(USER_ID) ?: EMPTY

    init {
        viewModelScope.launch {
            userRepository.getUser(loggedUserId) { user ->
                _uiLiveData.value = AllTabsUiModel(
                    user?.picture ?: EMPTY,
                    emptyList()
                )
            }
        }
    }

}