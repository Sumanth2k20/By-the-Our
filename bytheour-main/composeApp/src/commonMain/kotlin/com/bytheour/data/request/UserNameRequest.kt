package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class UserNameRequest(
    @SerialName("field")
    val username: String?= "",
)


@Serializable
data class UserEmailRequest(
    @SerialName("field")
    val userMail: String?= "",
)

