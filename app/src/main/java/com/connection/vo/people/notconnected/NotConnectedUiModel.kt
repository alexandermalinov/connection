package com.connection.vo.people.notconnected

import com.connection.vo.people.PeopleUiModel

class NotConnectedUiModel(
    val notConnections: List<NotConnectedPeopleListItemUiModel> = emptyList()
) : PeopleUiModel()