package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AddAddressResponse(
    val `data`: Data = Data(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable

    data class Data(
        @SerialName("address")
        val address: Address = Address(),
        @SerialName("house")
        val house: String = "", // Satya Mev
        @SerialName("id")
        val id: String = "", // U6MeXXq01g4Oi90rFLwG
        @SerialName("lat")
        val lat: String = "",
        @SerialName("lng")
        val lng: String = ""
    )

    @Serializable
    data class Address(
        @SerialName("city")
        val city: String = "", // Jamnagar
        @SerialName("line_1")
        val line1: String = "", //  Society
        @SerialName("line_2")
        val line2: String = "", // Opp.  Colony
        @SerialName("state")
        val state: String = "", // Gujarat
        @SerialName("zip")
        val zip: String = "" // 361006
    )

}

