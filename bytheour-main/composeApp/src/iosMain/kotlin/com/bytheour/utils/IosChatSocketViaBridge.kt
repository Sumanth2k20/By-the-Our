package com.bytheour.utils

import com.bytheour.data.response.Message
import com.bytheour.data.response.parseMessageFromJson
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

/**
 * Adapts the Swift-side bridge to the shared ChatSocket API.
 */
class IosChatSocketViaBridge(private val url: String) : ChatSocket {

    private val bridge: IosChatBridge
        get() = requireNotNull(IosChatBridgeRegistry.bridge) {
            println("SAI--IosChatBridge--IosChatBridge not registered")
            "IosChatBridge not registered. Call IosChatBridgeRegistry.register(...) in iOS App startup."
        }

    private val pendingJoins = mutableListOf<Pair<String, String>>()

    private val messageCh = Channel<Message>(Channel.BUFFERED)
    override val messages: Flow<Message> = messageCh.receiveAsFlow()

    private val connected = MutableStateFlow(false)
    override val isConnected: Flow<Boolean> = connected

    override fun connect() {
        // wire listeners once
        bridge.on("message", object : IosChatListener() {
            override fun onMessage(message: String) {
                println("SAI---rawJson---$message")
                messageCh.trySend(parseMessageFromJson(message))
            }
        })
        bridge.on("message-support", object : IosChatListener() {
            override fun onMessage(message: String) {
                println("SAI---SupportRawJson---$message")
                messageCh.trySend(parseMessageFromJson(message))
            }
        })
        bridge.on("connected", object : IosChatListener() {
            override fun onConnected() {
                println("SAI---onConnected---true")
                connected.tryEmit(true)
                pendingJoins.forEach { (evt, payload) -> bridge.emit(evt, payload) }
                pendingJoins.clear()
            }
        })
        bridge.on("disconnected", object : IosChatListener() {
            override fun onDisconnected() {
                println("SAI---onDisconnected---true")

                connected.tryEmit(false)
            }
        })

        bridge.connect(url)
    }

    override fun disconnect() {
        bridge.disconnect()
        connected.tryEmit(false)
    }

    override fun join(roomId: String, isSupport: Boolean, userType: String, fromUserId: String) {
        val payload = buildString {
            append("{\"room\":\"").append(roomId).append("\"")
            if (isSupport) {
                append(",\"admin\":false}")
            } else {
                append(",\"")
                append(userType)
                append("\":\"").append(fromUserId).append("\"}")
            }
        }
        // Always emit "join" just like Android
        println("SAI---join---$payload")
        if (connected.value) {
            bridge.emit("join", payload)
        } else {
            pendingJoins.add("join" to payload)
        }
    }

    override fun sendChatMessage(payloadJson: String, toSupportRoom: Boolean) {
        println("SAI---sendChatMessage---$payloadJson")
        bridge.emit(if (toSupportRoom) "message-support" else "message", payloadJson)
    }

    override fun emit(event: String, payloadJson: String) {
        println("SAI---even-$event---$payloadJson")
        bridge.emit(event, payloadJson)
    }
//    override fun joinChannel(payload: String) {
//        bridge.emit("join", payload)
//    }
//
//    override fun joinSupportChannel(payload: String) {
//        bridge.emit("join-support", payload)
//    }
//
//    override fun sendSupportMessage(payload: String) {
//        bridge.emit("message-support", payload)
//    }
}