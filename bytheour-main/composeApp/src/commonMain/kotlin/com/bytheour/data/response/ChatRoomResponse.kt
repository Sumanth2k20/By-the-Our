package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatRoomResponse(
    @SerialName("data")
    val data: CreateChatRoomData,
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class CreateChatRoomData (
        @SerialName("chat_room_id")
        var chatRoomId: String? = "",
        @SerialName("is_initial_chat")
        val isInitialChat: Int = 0
    )
}