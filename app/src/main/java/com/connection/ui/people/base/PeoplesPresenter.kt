package com.connection.ui.people.base

import com.connection.vo.people.PeopleListItemUiModel

interface PeoplesPresenter {

    fun onUserClick(user: PeopleListItemUiModel)

    fun onConnectClick(senderUser: PeopleListItemUiModel)

    fun onDiscoverClick()
}