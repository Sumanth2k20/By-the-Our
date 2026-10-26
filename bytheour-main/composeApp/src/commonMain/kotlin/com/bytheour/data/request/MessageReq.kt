package com.bytheour.data.request

import com.bytheour.data.response.Message
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageReq(
    @SerialName("message")
    var message: Message,
    @SerialName("room")
    var room: String?
)
