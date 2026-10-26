package com.bytheour.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Meta(
    @SerialName("api")
    var api: String? = null,
    @SerialName("message")
    var message: String? = null,
    @SerialName("url")
    var url: String? = null,
    @SerialName("unread_count")
    var unreadCount: Int? = null,
    @SerialName("amount")
    var amount: Amount? = Amount(),
    @SerialName("count")
    var count: Int? = null,
) {
    @Serializable
    data class Amount(
        @SerialName("wallet")
        var wallet: Double? = 0.0,
        @SerialName("hold")
        var hold: Double? = 0.0,
        @SerialName("process")
        var process: Double? = 0.0
    )
}