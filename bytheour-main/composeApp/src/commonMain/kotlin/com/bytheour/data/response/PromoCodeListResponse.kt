package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromoCodeListResponse(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
)  {
    @Serializable
    data class Data(
        @SerialName("code")
        val code: String = "", //
        @SerialName("description")
        val description: String = "", // Test Description
        @SerialName("image")
        val image: String = "", // https://bythehour.s3.ap-south-1.amazonaws.com/promo-codes/3Wp0OW7eWgInesy5dwE69SaWRKUDDMfYg7wFmx5l.png
        @SerialName("max_amount")
        val maxAmount: Double = 0.0, // 10
        @SerialName("title")
        val title: String = "", // Test Code
        @SerialName("type")
        val type: String = "", // fixed
        @SerialName("value")
        val value: Double = 0.0 // 10
    )
}