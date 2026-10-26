package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DefaultAddressResponse(
    @SerialName("data")
    val data: String? = "",
    @SerialName("meta")
    val meta: Meta = Meta()
)