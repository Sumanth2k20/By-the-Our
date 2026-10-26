package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonResponse(
    @SerialName("data")
    val data: String? = "",
    @SerialName("meta")
    val meta: Meta = Meta()
)

@Serializable
data class ModelCommon(
    val data: String? = "",
    val meta: Meta = Meta()
)