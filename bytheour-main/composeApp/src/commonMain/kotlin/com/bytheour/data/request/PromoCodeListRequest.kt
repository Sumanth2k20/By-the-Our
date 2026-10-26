package com.bytheour.data.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromoCodeListRequest(
    @SerialName("booking")
    val bookingId: String? = null,
)
