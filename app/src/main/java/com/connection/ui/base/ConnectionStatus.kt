package com.connection.ui.base

enum class ConnectionStatus(val status: String) {
    CONNECTED("connected"),
    NOT_CONNECTED("not connected"),
    REQUEST_SENT("sent"),
    REQUEST_RECEIVED("received")
}