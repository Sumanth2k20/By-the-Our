package com.bytheour.data.request

import kotlinx.serialization.SerialName

data class DefaultAddressRequest(
    @SerialName("id")
    val id: String? = "",
)
