package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetServiceProviderRequest(
    @SerialName("service")
    val service :String?= "",
    @SerialName("limit")
    val limit :String?= "",
    @SerialName("offset")
    val offset :String?= "",
    @SerialName("provider_name")
    val providerName :String?= "",
)
