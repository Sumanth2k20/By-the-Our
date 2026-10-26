package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelTaxClassification(
    val data: Data = Data(),
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("id")
        val id: String = "",
        @SerialName("type")
        val type: String = "",
        @SerialName("classification_number")
        val classificationNumber: Long = 0,
    )
}