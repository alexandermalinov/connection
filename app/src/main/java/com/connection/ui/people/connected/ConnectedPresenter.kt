package com.connection.ui.people.connected

import com.connection.vo.people.connected.ConnectedPeopleListItemUiModel

interface ConnectedPresenter {

    fun onChatClick(user: ConnectedPeopleListItemUiModel)
}