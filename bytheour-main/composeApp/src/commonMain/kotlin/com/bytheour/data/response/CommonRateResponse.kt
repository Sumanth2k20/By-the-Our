package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CommonRateResponse(
    @SerialName("data")
    var `data`: String? = "",
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Meta(
        @SerialName("api")
        var api: String = "",
        @SerialName("common")
        var common: Common? = null,
        @SerialName("message")
        var message: String = "",
        @SerialName("url")
        var url: String = ""
    )

    @Serializable
    data class Common(
        @SerialName("allowed")
        var allowed: Boolean = false,
        @SerialName("min_hour")
        var minHour: String = "",
        @SerialName("rate")
        var rate: String = "0.0"
    )
}