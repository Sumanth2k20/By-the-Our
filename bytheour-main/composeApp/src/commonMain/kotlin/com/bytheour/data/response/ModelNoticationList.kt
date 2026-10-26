package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ModelNotificationList(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("date")
        val date: String = "", // 03 September, 2021
        @SerialName("id")
        val id: String = "", // e7ba95e5-fe71-4138-813a-b62e0c4acd7f
        @SerialName("message")
        val message: String = "", // bella has booked you for Split AC Cleaning on 03 Sep, 2021 at 3PM.
        @SerialName("read")
        var read: Boolean = false, // false
        @SerialName("related_id")
        val relatedId: String = "", // BTHx802#27
        @SerialName("title")
        val title: String = "", // New Booking Requst
        @SerialName("type")
        val type: String = "",
        @SerialName("image")
        val image: String = "" // booking-request
    )
}