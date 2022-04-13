package com.connection.ui.people.notconnected

import com.connection.vo.people.notconnected.NotConnectedPeopleListItemUiModel

interface NotConnectedPresenter {

    fun onSendRequestClick(user: NotConnectedPeopleListItemUiModel)
}