package com.bytheour.data.request


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingServiceRequest(
    @SerialName("service")
    val service: String? = null,
    @SerialName("latitude")
    val latitude: String? = null,
    @SerialName("longitude")
    val longitude: String? = null,
    @SerialName("address")
    val address: String? = null,
    @SerialName("hours")
    val hours: String? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("timezone")
    val timeZone: String? = null,
    @SerialName("inspection_booking_id")
    val inspectionBookingId :String? = null
)
