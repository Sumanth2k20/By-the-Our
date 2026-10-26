package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddressListResponse(
    val `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("address")
        val address: Address = Address(),
        @SerialName("house")
        val house: String = "",
        @SerialName("name")
        val name: String = "",
        @SerialName("id")
        val id: String = "",
        @SerialName("lat")
        val lat: String = "",
        @SerialName("lng")
        val lng: String = "",
        @SerialName("default")
        var isDefault: Boolean = false,
        @SerialName("isSelected")
        var isSelected : Boolean = false,
        @SerialName("isEnable")
        var isEnable: Boolean = true
    )

    @Serializable
    data class Address(
        @SerialName("city")
        val city: String = "",
        @SerialName("line_1")
        val line1: String = "",
        @SerialName("line_2")
        val line2: String = "",
        @SerialName("state")
        val state: String = "",
        @SerialName("zip")
        val zip: String = ""
    ) {
        val fullAdd: String
            get() {
                return "$line1, $line2 $city-$state $zip"
            }
    }

}