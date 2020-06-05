package com.future.socket

data class SocketStatusModel(
    var status: String = STATUS_DEFAULT
) {
    companion object {

        const val STATUS_DEFAULT = "STATUS_DEFAULT"
        const val STATUS_OPEN = "STATUS_OPEN"
        const val STATUS_FAILURE = "STATUS_FAILURE"
        const val STATUS_CLOSED = "STATUS_CLOSED"
    }
}