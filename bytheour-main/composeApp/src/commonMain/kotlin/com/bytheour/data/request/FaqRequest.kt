package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FaqRequest(
    @SerialName("limit")
    val limit :Int?= null,
    @SerialName("offset")
    val offset :String?= null,
    @SerialName("type")
    val type :String?= null,
)