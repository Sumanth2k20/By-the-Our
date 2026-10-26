package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TicketSupportResponse(
    @SerialName("data")
    val `data`: ArrayList<SupportData> = arrayListOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
)

@Serializable
data class SupportData(
    @SerialName("booking")
    val booking: Booking = Booking(),
    @SerialName("account")
    val account: Account = Account(),
    @SerialName("conclusion")
    val conclusion: String = "",
    @SerialName("id")
    val id: String = "", // BTHx400404#U3
    @SerialName("status")
    var status: String = "" // Requested
)

@Serializable
data class Booking(
    @SerialName("date")
    val date: String = "", // 2021-09-23
    @SerialName("id")
    val id: String = "", // BTHx402#13
    @SerialName("time")
    val time: String = "", // 8AM - 9AM
    @SerialName("title")
    val title: String = "",
    @SerialName("service")
    val service: SupportService = SupportService()
)

@Serializable
data class Account(
    @SerialName("first_name")
    val firstName: String = "", // 2021-09-23
    @SerialName("last_name")
    val lastName: String = "", // BTHx402#13
    @SerialName("profile")
    val profile: String = ""
)

@Serializable
data class SupportService(
    @SerialName("id")
    val id: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("image")
    val image: String = "",
    @SerialName("category")
    val category: SupportCategory = SupportCategory()
)

@Serializable
data class SupportCategory(
    @SerialName("id")
    val id: String = "",
    @SerialName("title")
    val title: String = ""
)

