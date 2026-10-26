package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetSupportHistoryRequest(
    @SerialName("support_id")
    val supportId: String? = null,
    @SerialName("limit")
    val limit: String? = null,
    @SerialName("offset")
    val offset: String? = null
)