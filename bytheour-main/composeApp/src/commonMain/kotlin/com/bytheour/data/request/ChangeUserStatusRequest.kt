package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChangeUserStatusRequest(
    @SerialName("id")
    val id:String?= null,
    @SerialName("status")
    val status:String?= null,

)


@Serializable
data class AdminDeleteUserRequest(
    @SerialName("id")
    val id: String?= ""
)


@Serializable
data class AdminGetUserRequest(
    @SerialName("id")
    val id: String?= ""
)
