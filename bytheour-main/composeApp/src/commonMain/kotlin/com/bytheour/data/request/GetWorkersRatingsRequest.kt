package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetWorkersRatingsRequest(
//    @SerialName("service")
//    val service: String? = null,
    @SerialName("worker")
    val worker: String? = null,
    @SerialName("limit")
    val limit: String = "",
    @SerialName("offset")
    val offset: String = "",
    @SerialName("ratings")
    val ratings: String? = null
)

