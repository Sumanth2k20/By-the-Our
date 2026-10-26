package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServiceListRequest(
    @SerialName("limit")
    val limit: String?,
    @SerialName("offset")
    val offset: String?,
    @SerialName("keyword")
    val keyWord:String?= null
)

@Serializable
data class CancelBookingRequest(
    @SerialName("booking")
    val booking: String?=null,
    @SerialName("reason_id")
    val reasonId: String?=null,
    @SerialName("text")
    val text: String?=null,
    @SerialName("hour_type")
    val hourType: String?=null,
    @SerialName("cancel_after_start_time")
    val cancelAfterStartTime: String?=null
)