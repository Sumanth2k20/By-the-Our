package com.bytheour.data.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailsRequest(
    @SerialName("service")
    val serviceId: String?= null,
    @SerialName("latitude")
    val latitude: String?= null,
    @SerialName("longitude")
    val longitude: String?= null,
)
