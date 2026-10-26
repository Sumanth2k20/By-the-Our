package com.bytheour.utils

import com.bytheour.data.response.Message
import kotlinx.coroutines.flow.Flow

interface ChatSocket {
    fun connect()
    fun disconnect()
    //fun emit(event: String, message: String)
//    fun joinChannel(payload: String)
//    fun joinSupportChannel(payload: String)
//    fun sendSupportMessage(payload: String)

    /**
     * Mimics your old onSocketConnected() joining logic.
     * - roomId       : "room"
     * - isSupport    : joins "join-support" and uses "admin":false
     * - userIsUser   : true -> {"user": fromUserId} else {"worker": fromUserId}
     */
    fun join(roomId: String, isSupport: Boolean, userType: String, fromUserId: String)

    /** Send a chat message (to "message" or "message-support" depending on flag). */
    fun sendChatMessage(payloadJson: String, toSupportRoom: Boolean = false)

    /** Raw emit if you need anything else. */
    fun emit(event: String, payloadJson: String)

    /** Stream of parsed messages for the UI (both "message" & "message-support"). */
    val messages: Flow<Message>

    /** Optional connectivity if you want it in UI. */
    val isConnected: Flow<Boolean>

}