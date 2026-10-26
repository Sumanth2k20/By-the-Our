package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AcceptAndDeclineRequest (
    @SerialName("booking")
    val booking:String?= null,
    @SerialName("timezone")
    val timezone:String?= null
)
@Serializable
data class PaymentDetailsRequest (
    @SerialName("stripe_version")
    val stripeVersion:String?= null,
)

@Serializable
data class StartBookingRequest (
    @SerialName("booking")
    val booking:String?= null,
)



@Serializable
data class RaiseMediationRequest(
    @SerialName("booking")
    val booking :String?= null,
    @SerialName("mediation_reason")
    val mediationReason:String?= null,
    @SerialName("mediation_amount")
    val mediationAmount :String? = null
)

@Serializable
data class ManageQuotationRequest(
    @SerialName("booking_id")
    val bookingId :String?= null,
    @SerialName("status")
    val status :String?= null,
    @SerialName("user_time")
    val userTime :String?= null,
    @SerialName("booking_time")
    val bookingTime :String?= null,
)