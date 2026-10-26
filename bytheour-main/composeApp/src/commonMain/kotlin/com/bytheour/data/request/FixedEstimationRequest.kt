package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FixedEstimationRequest(
    @SerialName("booking_id")
    val bookingId: String?,
    @SerialName("quoted_hours")
    val estimatedHours: String?,
    @SerialName("price_type")
    val priceType: String?,
    @SerialName("fixed_price")
    val fixedPrice: String?,
    @SerialName("start_time")
    val startTime: List<String?> = emptyList(),
    @SerialName("end_time")
    val endTime: List<String?> = emptyList()
)
@Serializable
data class EstimationRequest(
    @SerialName("booking_id")
    val bookingId: String?,
    @SerialName("quoted_hours")
    val estimatedHours: String?,
    @SerialName("price_type")
    val priceType: String?,
    @SerialName("start_time")
    val startTime: List<String?> = emptyList(),
    @SerialName("end_time")
    val endTime: List<String?> = emptyList()
)