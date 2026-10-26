package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelFaqResponse(
    @SerialName("Faqs")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("Question")
        val question: String = "",
        @SerialName("Answer")
        val answer: String = "",
        @SerialName("Num")
        val type: String = "",
        @SerialName("isExpand")
        var isExpand: Boolean = false
    )
}