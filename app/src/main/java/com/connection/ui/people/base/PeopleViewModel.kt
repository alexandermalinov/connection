package com.connection.ui.people.base

import androidx.core.os.bundleOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.connection.R
import com.connection.data.api.model.UserData
import com.connection.data.repository.user.UserRepository
import com.connection.navigation.NavigationGraph
import com.connection.navigation.NestedFragmentGraph
import com.connection.ui.base.BaseViewModel
import com.connection.utils.common.Constants.CONNECTED_PEOPLE_TAB_POSITION
import com.connection.utils.common.Constants.EMPTY
import com.connection.utils.common.Constants.FRAGMENT_CONNECTED_PEOPLE
import com.connection.utils.common.Constants.FRAGMENT_NOT_CONNECTED_PEOPLE
import com.connection.utils.common.Constants.FRAGMENT_INVITATION_PEOPLE
import com.connection.utils.common.Constants.HEADER_MODEL
import com.connection.utils.common.Constants.INVITED_PEOPLE_TAB_POSITION
import com.connection.utils.common.Constants.NOT_CONNECTED_PEOPLE_TAB_POSITION
import com.connection.vo.people.PeopleListItemUiModel
import com.connection.vo.people.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class PeopleViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel(), PeoplesPresenter {

    /* --------------------------------------------------------------------------------------------
     * Properties
    ---------------------------------------------------------------------------------------------*/
    val selectedTab: LiveData<Int>
        get() = _selectedTab

    protected var loggedUser: UserData? = null
    private val _selectedTab = MutableLiveData(CONNECTED_PEOPLE_TAB_POSITION)

    init {
        viewModelScope.launch {
            initUserData()
        }
        //onTabSelected(FRAGMENT_CONNECTED_PEOPLE)
    }

    /* --------------------------------------------------------------------------------------------
     * Exposed
    ---------------------------------------------------------------------------------------------*/
    fun onTabClick(tabPosition: Int) {
        when (tabPosition) {
            CONNECTED_PEOPLE_TAB_POSITION -> FRAGMENT_CONNECTED_PEOPLE
            NOT_CONNECTED_PEOPLE_TAB_POSITION -> FRAGMENT_NOT_CONNECTED_PEOPLE
            else -> FRAGMENT_INVITATION_PEOPLE
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
        _selectedTab.value = getTabPosition(tab)
    }

    private fun getTabPosition(tab: String) = when (tab) {
        FRAGMENT_CONNECTED_PEOPLE -> CONNECTED_PEOPLE_TAB_POSITION
        FRAGMENT_NOT_CONNECTED_PEOPLE -> NOT_CONNECTED_PEOPLE_TAB_POSITION
        else -> INVITED_PEOPLE_TAB_POSITION
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
        _navigationLiveData.value = NavigationGraph(R.id.not_connected_people)
    }
}