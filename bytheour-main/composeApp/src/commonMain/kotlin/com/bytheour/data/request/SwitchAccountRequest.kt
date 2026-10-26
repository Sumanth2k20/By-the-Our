package com.bytheour.data.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SwitchAccountRequest(
    @SerialName("contact")
    val contact: String?,
    @SerialName("security_token")
    val securityToken: String?,
    @SerialName("type")
    val type: String?= null,
    @SerialName("email")
    val email: String?= null,
)

