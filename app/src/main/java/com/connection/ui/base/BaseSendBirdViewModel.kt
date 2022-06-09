package com.connection.ui.base

import androidx.lifecycle.viewModelScope
import com.connection.data.api.model.user.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class BaseSendBirdViewModel(
    private val chatTabRepository: ChatTabRepository
) : BaseViewModel() {

    /* --------------------------------------------------------------------------------------------
     * Protected
    ---------------------------------------------------------------------------------------------*/
    protected fun connectUser(user: UserData?) {
        viewModelScope.launch {
            user?.let {
                chatTabRepository.connectUser(
                    user = it,
                    onFailure = { Timber.e("error occurred while trying to connect user") }
                )
            }
        }
    }
}