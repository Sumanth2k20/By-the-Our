package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetServicesRequest(
    @SerialName("category_id")
    val categoryId: String?,
    @SerialName("limit")
    val limit: String?,
    @SerialName("keyword")
    val keyword: String?= null,
    @SerialName("offset")
    val offset: String?,
)