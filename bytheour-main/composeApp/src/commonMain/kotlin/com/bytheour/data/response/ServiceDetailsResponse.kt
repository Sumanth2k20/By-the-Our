package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceDetailsResponse(
    @SerialName("data")
    val data: ServiceData? = null,
    @SerialName("meta")
    val meta: MetaX?
)

@Serializable
data class ServiceData(
    @SerialName("id")
    val id: String?,
    @SerialName("max_rate")
    val maxRate: Int?,
    @SerialName("min_rate")
    val minRate: Int?,
    @SerialName("title")
    val title: String?
)

