package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateInspectionRequest(
    @SerialName("service")
    val service: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("timeZone")
    val timeZone: String? = "Asia/Kolkata",
)
