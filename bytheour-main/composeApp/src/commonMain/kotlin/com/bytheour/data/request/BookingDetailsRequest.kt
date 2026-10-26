package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingDetailsRequest (
    @SerialName("id")
    val id:String?= null
)


@Serializable
data class AddSupportTicketRequest(
    @SerialName("booking")
    val booking:String?= null
)
@Serializable
data class CloseSupportTicketRequest(
    @SerialName("support")
    val supportId:String?= null
)


@Serializable
data class GetWorkerSpecificServicesRequest(
    @SerialName("worker_id")
    val workerId:String?= null
)