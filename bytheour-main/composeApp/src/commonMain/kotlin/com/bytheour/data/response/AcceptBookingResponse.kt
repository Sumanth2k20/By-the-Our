package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcceptBookingResponse(
    @SerialName("data")
    val `data`: String?,
    @SerialName("meta")
    val meta: MetaX?
)