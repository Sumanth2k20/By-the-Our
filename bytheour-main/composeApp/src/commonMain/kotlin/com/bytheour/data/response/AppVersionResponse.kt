package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppVersionResponse(
    @SerialName("AndriodVersion")
    val andriodVersion: String? = "",
    @SerialName("IOSVersion")
    val iOSVersion: String = "",

    )
