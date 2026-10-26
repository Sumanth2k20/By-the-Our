package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ManageQuotationResponse(
    @SerialName("data")
    var `data`: Data? = null,
    @SerialName("meta")
    var meta: ModelPaymentIntent.Meta? = null
) {
    @Serializable
    data class Data(
        @SerialName("id")
        var id: String? = null,
        @SerialName("amount")
        var amount: String? = null,
    )
}


@Serializable
data class ModelPaymentIntent(
    @SerialName("data")
    val `data`: Data = Data(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val id: String = "", //
        @SerialName("amount")
        val amount: Int = 0,
        @SerialName("booking")
        val booking: String = ""
    )

    @Serializable
    data class Meta(
        @SerialName("api")
        var api: String? = null,
        @SerialName("message")
        var message: String? = null,
        @SerialName("url")
        var url: String? = null,
        @SerialName("wallet_amount")
        var walletAmount: Double = 0.0,
        @SerialName("booking_completed")
        var bookingCompleted: Boolean = false
    )
}