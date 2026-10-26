package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransactionListResponse(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("amount")
        val amount: Double = 0.0,
        @SerialName("id")
        val id: String = "",
        @SerialName("message")
        val message: String = "",
        @SerialName("status")
        val status: String = "",
        @SerialName("type")
        val type: String = "",
        @SerialName("date")
        val date: String = "",
        @SerialName("transactions_type")
        val transactionsType: String = "",
        @SerialName("booking")
        val booking: String? = null,
        @SerialName("withdrawal")
        val withdrawal: Withdrawal? = null
    ) {
        @Serializable
        data class Withdrawal(
            @SerialName("approved")
            val approved: Boolean = false,
            @SerialName("comment")
            val comment: String = "",
            @SerialName("file")
            val file: String = "",
        )
    }
}