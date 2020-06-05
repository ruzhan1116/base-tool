package com.future.socket

import android.os.Handler
import android.os.Looper
import android.util.Log
import okhttp3.*


class WebSocketManager private constructor() {

    companion object {

        private const val TAG = "WebSocketManager"
        private const val EMPTY_JSON = "{}"

        private const val DEFAULT_HEARTBEAT_TIME = 1000 * 60L
        private const val DEFAULT_RECONNECT_TIME = 1000 * 6L

        @Volatile
        private var INSTANCE: WebSocketManager? = null

        @JvmStatic
        fun get() = INSTANCE ?: synchronized(WebSocketManager::class.java) {
            INSTANCE ?: WebSocketManager().also {
                INSTANCE = it
            }
        }
    }

    var socketHost = ""
    var heartbeatTime = DEFAULT_HEARTBEAT_TIME
    var reconnectTime = DEFAULT_RECONNECT_TIME
    var isOpenHeartbeat = false
    var isOpenReconnect = false

    var webSocket: WebSocket? = null
    val socketStatusModel = SocketStatusModel()

    private val handler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }
    private val heartbeatTask: HeartbeatTask by lazy {
        HeartbeatTask()
    }
    private val reconnectTask: ReconnectTask by lazy {
        ReconnectTask()
    }
    var webSocketListener: WebSocketListener? = null

    fun connect() {
        if (socketStatusModel.status == SocketStatusModel.STATUS_OPEN) {
            webSocket?.cancel()
        }
        removeHeartbeat()
        removeReconnect()
        // start web socket
        val request = Request.Builder()
            .url(socketHost)
            .build()
        val okHttpClient = OkHttpClient()
        val escapeWebSocketListener = EscapeWebSocketListener()
        webSocket = okHttpClient.newWebSocket(request, escapeWebSocketListener)
        okHttpClient.dispatcher.executorService.shutdown()
    }

    fun cancel() {
        webSocket?.cancel()
        removeHeartbeat()
        removeReconnect()
    }

    private fun startHeartbeat() {
        if (isOpenHeartbeat) {
            handler.removeCallbacks(heartbeatTask)
            handler.postDelayed(heartbeatTask, heartbeatTime)
        }
    }

    private fun startReconnect() {
        if (isOpenReconnect) {
            handler.removeCallbacks(reconnectTask)
            handler.postDelayed(reconnectTask, reconnectTime)
        }
    }

    private fun removeHeartbeat() {
        if (isOpenHeartbeat) {
            handler.removeCallbacks(heartbeatTask)
        }
    }

    private fun removeReconnect() {
        if (isOpenReconnect) {
            handler.removeCallbacks(reconnectTask)
        }
    }

    fun setHeartbeat(isOpenHeartbeat: Boolean, jsonText: String) {
        this.isOpenHeartbeat = isOpenHeartbeat
        heartbeatTask.heartbeatJson = jsonText
    }

    fun setReconnect(isOpenReconnect: Boolean) {
        this.isOpenReconnect = isOpenReconnect
    }

    fun isJsonText(text: String): Boolean {
        return text.isNotBlank() &&
                text.startsWith("{") &&
                text.endsWith("}")
    }

    private inner class EscapeWebSocketListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: Response) {
            super.onOpen(webSocket, response)
            Log.i(TAG, "onOpen called...")
            socketStatusModel.status = SocketStatusModel.STATUS_OPEN
            startHeartbeat()
            webSocketListener?.onOpen(webSocket, response)
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            super.onMessage(webSocket, text)
            Log.i(TAG, "onMessage called... text: $text")
            webSocketListener?.onMessage(webSocket, text)
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosing(webSocket, code, reason)
            Log.i(TAG, "onClosing called...")
            webSocketListener?.onClosing(webSocket, code, reason)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            super.onClosed(webSocket, code, reason)
            Log.i(TAG, "onClosed called...")
            socketStatusModel.status = SocketStatusModel.STATUS_CLOSED
            webSocketListener?.onClosed(webSocket, code, reason)
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
            super.onFailure(webSocket, t, response)
            Log.i(TAG, "onFailure called... $t")
            socketStatusModel.status = SocketStatusModel.STATUS_FAILURE
            startReconnect()
            webSocketListener?.onFailure(webSocket, t, response)
        }
    }

    inner class ReconnectTask : Runnable {

        override fun run() {
            Log.i(TAG, "ConnectTask called...")
            connect()
        }
    }

    inner class HeartbeatTask : Runnable {

        var heartbeatJson = EMPTY_JSON

        override fun run() {
            val jsonText = heartbeatJson
            if (socketStatusModel.status != SocketStatusModel.STATUS_FAILURE) {
                webSocket?.send(jsonText)
                handler.postDelayed(heartbeatTask, heartbeatTime)
            }
            Log.i(TAG, "HeartbeatTask called... jsonText: $jsonText")
        }
    }
}