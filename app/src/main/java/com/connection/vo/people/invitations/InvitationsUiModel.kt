package com.connection.vo.people.invitations

import com.connection.vo.people.PeopleUiModel

class InvitationsUiModel(
    val invitations: List<InvitationListItemUiModel> = emptyList()
) : PeopleUiModel()