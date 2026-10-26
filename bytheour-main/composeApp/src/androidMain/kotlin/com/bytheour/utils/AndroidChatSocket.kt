package com.bytheour.utils

import android.util.Log
import com.bytheour.data.response.Message
import com.bytheour.data.response.parseMessageFromJson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import org.json.JSONObject

class AndroidChatSocket(private val url: String) : ChatSocket {


    private var socket: Socket? = null
    private val connected = MutableStateFlow(false)
    override val isConnected: Flow<Boolean> = connected

    private val messageCh = Channel<Message>(Channel.BUFFERED)
    override val messages: Flow<Message> = messageCh.receiveAsFlow()

    private val onConnect = Emitter.Listener {
        connected.tryEmit(true)
    }
    private val onDisconnect = Emitter.Listener {
        connected.tryEmit(false)
    }
    private val onError = Emitter.Listener { args ->
        // optional: log args[0]
    }
    private val onMessage = Emitter.Listener { args ->
        args.firstOrNull()?.toString()?.let {
            Log.e("SAI---onMessageReceived---", it)
            messageCh.trySend(parseMessageFromJson(it))
        }
    }

    override fun connect() {
        if (socket != null) return
        socket = IO.socket(url, IO.Options()).also { s ->
            s.on(Socket.EVENT_CONNECT, onConnect)
            s.on(Socket.EVENT_DISCONNECT, onDisconnect)
            s.on(Socket.EVENT_CONNECT_ERROR, onError)
            s.on("went-wrong", onError)
            s.on("message", onMessage)
            s.on("message-support", onMessage)
            s.connect()
            println("ChatSCOCKET CONNECTED =====================$onConnect")
        }
    }

    override fun disconnect() {
        socket?.apply {
            off(Socket.EVENT_CONNECT, onConnect)
            off(Socket.EVENT_DISCONNECT, onDisconnect)
            off(Socket.EVENT_CONNECT_ERROR, onError)
            off("went-wrong", onError)
            off("message", onMessage)
            off("message-support", onMessage)
            disconnect()
            println("ChatSCOCKET DiSCONNECTED =====================$onConnect")
        }
        socket = null
        connected.tryEmit(false)
    }

    override fun join(roomId: String, isSupport: Boolean, userType: String, fromUserId: String) {
        val obj = JSONObject().apply {
            put("room", roomId)
            if (isSupport) {
                put("admin", false)
                // server expects "join-support"
                socket?.emit("join-support", this)
                return
            } else {
                put(userType, fromUserId)
            }
        }
        socket?.emit("join", obj)
    }

    override fun sendChatMessage(payloadJson: String, toSupportRoom: Boolean) {
        val event = if (toSupportRoom) "message-support" else "message"
        socket?.emit(event, JSONObject(payloadJson))
    }

    override fun emit(event: String, payloadJson: String) {
        socket?.emit(event, JSONObject(payloadJson))
    }

//    override fun joinChannel(payload: String) {
//        socket?.emit("join", JSONObject(payload))
//    }
//
//    override fun joinSupportChannel(payload: String) {
//        socket?.emit("join-support", JSONObject(payload))
//    }
//
//    override fun sendSupportMessage(payload: String) {
//        socket?.emit("message-support", JSONObject(payload))
//    }
}