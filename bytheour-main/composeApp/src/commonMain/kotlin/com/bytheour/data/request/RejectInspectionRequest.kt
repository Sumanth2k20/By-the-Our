package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RejectInspectionRequest(
    @SerialName("booking")
    val booking: String? = null,
    @SerialName("reason")
    val reason: String? = null,
)
