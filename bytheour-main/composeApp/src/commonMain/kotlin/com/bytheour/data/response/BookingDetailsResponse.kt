package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingDetailResponse(
    @SerialName("data")
    var `data`: ModelProviderBookingList.Data = ModelProviderBookingList.Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
)
