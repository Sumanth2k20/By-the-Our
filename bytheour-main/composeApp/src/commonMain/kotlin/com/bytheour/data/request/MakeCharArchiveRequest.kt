package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MakeCharArchiveRequest(
    @SerialName("room_id")
    val room_id: String?= null,
    @SerialName("unarchive")
    val unarchive: Int?= null
)