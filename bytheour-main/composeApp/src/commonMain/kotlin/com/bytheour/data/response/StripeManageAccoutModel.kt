package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StripeManageAccountModel(@SerialName("data")
                                    var data: StripAccountData = StripAccountData(),
                                   @SerialName("meta")
                                    var meta: StripAccountMeta = StripAccountMeta()
) {
    @Serializable
    data class StripAccountData(
        @SerialName("status")
        var status: String? = "",
        @SerialName("url")
        var url: String? = "",
    )
    @Serializable
    data class StripAccountMeta(
        @SerialName("message")
        var message: String? = "",
        @SerialName("url")
        var url: String? = "",
        @SerialName("api")
        var api: String? = ""
    )
}