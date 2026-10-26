package com.bytheour.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddAddressRequest(
    @SerialName("name")
    val name :String?= "",
    @SerialName("house")
    val house :String?= "",
    @SerialName("address_line_1")
    val addressLine1 :String?= "",
    @SerialName("state")
    val state :String?= "",
    @SerialName("city")
    val city :String?= "",
    @SerialName("zip_code")
    val zipCode :String?= "",
    @SerialName("lat")
    val latitude :String?= "",
    @SerialName("lng")
    val longitude :String?= "",
    @SerialName("id")
    val id :String?= "",
)
