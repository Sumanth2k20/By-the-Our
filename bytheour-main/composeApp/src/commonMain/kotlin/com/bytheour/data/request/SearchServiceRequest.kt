package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchServiceRequest (
    @SerialName("limit")
    val limit: String?,
    @SerialName("offset")
    val offset: String?,
    @SerialName("keyword")
    val searchKey: String?=null
)
