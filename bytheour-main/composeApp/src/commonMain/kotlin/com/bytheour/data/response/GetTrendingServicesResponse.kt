package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetTrendingServicesResponse(
    @SerialName("data")
    var `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("description")
        var description: String = "",
        @SerialName("services_booked_last_month")
        var bookingCount: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("image")
        var image: String = "",
        @SerialName("title")
        var title: String = "",
        @SerialName("category")
        var category: Category = Category(),
        @SerialName("isCheck")
        var isCheck: Boolean = false,
        @SerialName("rate")
        var inspectionRate: Double = 0.0
    )

    @Serializable
    data class Category(
        @SerialName("id")
        var id: String = "",
        @SerialName("title")
        var title: String = ""
    )


}