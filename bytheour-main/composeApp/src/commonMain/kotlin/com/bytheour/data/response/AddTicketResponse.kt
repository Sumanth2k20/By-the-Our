package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddSupportTicketResponse(
    @SerialName("data")
    var `data`: Data = Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("booking")
        var booking: Booking = Booking(),
        @SerialName("conclusion")
        var conclusion: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("status")
        var status: String = ""
    )

    @Serializable
    data class Booking(
        @SerialName("date")
        var date: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("time")
        var time: String = "",
        @SerialName("title")
        var title: String = ""
    )


}