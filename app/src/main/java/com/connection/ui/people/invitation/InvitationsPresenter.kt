package com.connection.ui.people.invitation

import com.connection.vo.people.invitations.InvitationListItemUiModel

interface InvitationsPresenter {

    fun onAcceptClick(invitation: InvitationListItemUiModel)

    fun onDeclineClick(invitation: InvitationListItemUiModel)
}