package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class  AddRatingRequest(
    @SerialName("booking")
    val booking:String?= null,
    @SerialName("service_rating")
    val serviceRating:String? =null,
    @SerialName("rating")
    val rating:String? =null,
    @SerialName("review")
    val review:String? =null
)