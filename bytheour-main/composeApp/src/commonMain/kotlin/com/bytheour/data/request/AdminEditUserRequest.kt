package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AdminEditUserRequest(
    @SerialName("id")
    val id: String,
    @SerialName("first_name")
    val firstName: String,
    @SerialName("last_name")
    val lastName: String,
    @SerialName("profile")
    val profile: String,
    @SerialName("email")
    val email: String,
)