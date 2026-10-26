package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsersListRequest(
    @SerialName("limit")
    val limit: String?,
    @SerialName("offset")
    val offset: String?,
    @SerialName("keyword")
    val searchKey: String?=null
)

@Serializable
data class DeleteServiceRequest(
    @SerialName("service")
    val service:String?=null
)


@Serializable
data class CommonRateRequest(
    @SerialName("common")
    val common:String?=null,
    @SerialName("rate")
    val rate:String?=null,
    @SerialName("min_hour")
    val min_hour:String?=null
)
@Serializable
data class ServiceByWorkerRequest(
    @SerialName("category_id")
    val category_id:String?=null,
    @SerialName("title")
    val title:String?=null,
    @SerialName("description")
    val description:String?=null
)



