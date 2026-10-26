package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetCardsResponse(
    @SerialName("data")
    val `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("exp_month")
        val expMonth: Int = 0, // 4
        @SerialName("exp_year")
        val expYear: Int = 0, // 2025
        @SerialName("id")
        val id: String = "", //
        @SerialName("last4")
        val last4: String = "", // 4242
        @SerialName("live")
        val live: Boolean = false, // false
        @SerialName("object")
        val objectX: String = "", // payment_method(card)
        @SerialName("type")
        val type: String = "" // visa
    )
}