package com.bytheour.utils

import kotlin.concurrent.Volatile


/**
 * IMPORTANT: Swift can SUBCLASS Kotlin open/abstract classes, but cannot implement Kotlin interfaces.
 * So we expose an open class that Swift will subclass.
 *
 * Also, Swift can call back into Kotlin through open classes.
 */
open class IosChatListener {
    open fun onMessage(message: String) {}
    open fun onConnected() {}
    open fun onDisconnected() {}
    open fun onError(msg: String) {}
}

open class IosChatBridge {
    open fun connect(url: String) {}
    open fun disconnect() {}
    open fun emit(event: String, payloadJson: String) {}
    open fun on(event: String, listener: IosChatListener) {}
}

/**
 * Registry pattern (same as your IosOtpBridgeRegistry).
 * The iOS app will register its Swift implementation during app startup.
 */
object IosChatBridgeRegistry {
    @Volatile
    var bridge: IosChatBridge? = null

    fun register(impl: IosChatBridge) {
        bridge = impl
    }
}