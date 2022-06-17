package com.connection.ui.base

import com.connection.utils.common.Constants.RECEIVED_INVITES
import com.connection.utils.common.Constants.SENT_INVITES

enum class InviteTypes(type: String) {
    RECEIVED_INVITE(RECEIVED_INVITES),
    SENT_INVITE(SENT_INVITES)
}