package com.bytheour.data.response


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaymentDetailsResponse(
    @SerialName("data")
    val `data`: PaymentData?,
    @SerialName("meta")
    val meta: PaymentMeta?
)
@Serializable
data class PaymentData(
    @SerialName("associated_objects")
    val associatedObjects: List<AssociatedObject?>?,
    @SerialName("created")
    val created: Int?,
    @SerialName("expires")
    val expires: Int?,
    @SerialName("id")
    val id: String?,
    @SerialName("livemode")
    val livemode: Boolean?,
    @SerialName("object")
    val objectX: String?,
    @SerialName("secret")
    val secret: String?
)
@Serializable
data class PaymentMeta(
    @SerialName("api")
    val api: String?,
    @SerialName("message")
    val message: String?,
    @SerialName("url")
    val url: String?
)