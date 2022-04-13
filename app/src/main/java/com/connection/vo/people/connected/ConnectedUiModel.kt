package com.connection.vo.people.connected

import com.connection.vo.people.PeopleUiModel

class ConnectedUiModel(
    val connections: List<ConnectedPeopleListItemUiModel> = emptyList()
) : PeopleUiModel()