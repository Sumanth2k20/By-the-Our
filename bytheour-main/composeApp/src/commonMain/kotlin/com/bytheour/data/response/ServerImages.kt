package com.bytheour.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Serializable
data class ServerImages(
    @SerialName("custom_id")
    val customId : String?=null,
    @SerialName("start_date")
    val startDate : String?=null,
    @SerialName("end_date")
    val endDate : String?=null,
    @SerialName("name")
    val name : String?=null,
    @SerialName("path")
    val path : String?=null
)
