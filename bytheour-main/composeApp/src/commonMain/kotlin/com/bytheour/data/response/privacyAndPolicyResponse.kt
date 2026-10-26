package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PrivacyAndPolicyResponse(
    @SerialName("content")
    val content: String,
    @SerialName("meta")
    val meta: MetaX?
)

@Serializable
data class MetaX(
    @SerialName("api")
    val api: String?,
    @SerialName("message")
    val message: String?,
    @SerialName("url")
    val url: String?
)