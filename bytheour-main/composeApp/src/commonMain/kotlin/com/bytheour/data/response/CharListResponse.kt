package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatListRoomResponse(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
){
    @Serializable
    data class Data(
        @SerialName("date")
        val date: String = "", // 1 week ago
        @SerialName("id")
        val id: String = "", // BTHx802#28
        @SerialName("user")
        val user: User = User(),
        @SerialName("worker")
        val worker: Worker = Worker(),
        @SerialName("is_initial_chat")
        val isInitialChat: Int = 0
    )

    @Serializable
    data class User(
        @SerialName("first_name")
        val firstName: String = "", // bella
        @SerialName("last_name")
        val lastName: String = "", // Swan
        @SerialName("profile")
        val profile: String = "" // https://bythehour.s3.ap-south-1.amazonaws.com/default/user.png
    )

    @Serializable
    data class Worker(
        @SerialName("id")
        val id: String = "",
        @SerialName("first_name")
        val firstName: String = "", // John
        @SerialName("last_name")
        val lastName: String = "", // Day
        @SerialName("profile")
        val profile: String = "" // https://bythehour.s3.ap-south-1.amazonaws.com/default/user.png
    )

}