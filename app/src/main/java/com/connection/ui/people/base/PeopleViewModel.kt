package com.connection.ui.people.base

import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.repository.chattab.ChatTabRepository
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.navigation.NestedFragmentGraph
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.CONNECTED_PEOPLE_TAB_POSITION
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.FRAGMENT_CONNECTED_PEOPLE
import com.connection.utils.common.Constants.FRAGMENT_NOT_CONNECTED_PEOPLE
import com.connection.utils.common.Constants.FRAGMENT_REQUESTS_PEOPLE
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.NOT_CONNECTED_PEOPLE_TAB_POSITION
import com.connection.vo.people.PeopleListItemUiModel
import com.connection.vo.people.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PeopleViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val chatTabRepository: ChatTabRepository
) : BaseViewModel(), PeoplesPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    protected var loggedUser: UserData? = null

    init {
        viewModelScope.launch {
            initUserData()
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
    ---------------------------------------------------------------------------------------------*/
    fun onTabClick(tabPosition: Int) {
        when (tabPosition) {
            CONNECTED_PEOPLE_TAB_POSITION -> FRAGMENT_CONNECTED_PEOPLE
            NOT_CONNECTED_PEOPLE_TAB_POSITION -> FRAGMENT_NOT_CONNECTED_PEOPLE
            else -> FRAGMENT_REQUESTS_PEOPLE
        }.let { tab ->
            onTabSelected(tab)
        }
    }

    /* --------------------------------------------------------------------------------------------
     * Private
    ---------------------------------------------------------------------------------------------*/
    private suspend fun initUserData() {
        userRepository.getLoggedUser() {
            loggedUser = it
        }
    }

    private fun onTabSelected(tab: String) {
        _navigationLiveData.value = NestedFragmentGraph(tab, R.id.container_view_people)
    }

    private fun navigateToChat(senderUser: PeopleListItemUiModel) {
        _navigationLiveData.value = NavigationGraph(
            R.id.action_peopleFragment_to_connectionChatFragment,
            bundleOf(HEADER_MODEL to senderUser.toUiModel(EMPTY))
        )
    }

    /* --------------------------------------------------------------------------------------------
     * Override
    ---------------------------------------------------------------------------------------------*/
    override fun onUserClick(user: PeopleListItemUiModel) {
        navigateToChat(user)
    }

    override fun onConnectClick(senderUser: PeopleListItemUiModel) {
        viewModelScope.launch {
            loggedUser?.let {
                navigateToChat(senderUser)
            }
        }
    }

    override fun onDiscoverClick() {
        _navigationLiveData.value =
            NavigationGraph(R.id.action_connectedPeopleFragment_to_notConnectedPeople)
    }
}