package com.bytheour.data.response

import com.bytheour.data.model.response.ServerImages
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetWorkersSpecificServicesResponse(
    @SerialName("data")
    var `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("id")
        var id: String = "",
        @SerialName("min_hour")
        var minHour: Int = 0,
        @SerialName("rate")
        var rate: Double = 0.0,
        @SerialName("service_at_premises")
        var serviceAtPremises: String = "",
        @SerialName("experience")
        var experience: Float? = 0f,
        @SerialName("description")
        var description: String? = "",
        @SerialName("radius")
        var radius: Float? = 0f,
        @SerialName("service")
        var service: Service = Service(),
        @SerialName("category")
        var category: Category = Category(),
        @SerialName("portfolio")
        var portfolio: Portfolio = Portfolio(),
        @SerialName("images")
        val images : ArrayList<ServerImages>?= null,
    )

    @Serializable
    data class Portfolio(
        @SerialName("service_ratings")
        var serviceRatings: String? = "0.0",
        @SerialName("service_rating_text")
        var serviceRatingText: String = "",
    )

    @Serializable
    data class Service(
        @SerialName("category")
        var category: Category = Category(),
        @SerialName("id")
        var id: String = "",
        @SerialName("image")
        var image: String = "",
        @SerialName("title")
        var title: String = "",
        @SerialName("description")
        var description: String = "",
        @SerialName("category_title")
        var categoryTitle: String = ""
    )

    @Serializable
    data class Category(
        @SerialName("id")
        var id: String = "",
        @SerialName("title")
        var title: String = ""
    )

    @Serializable
    data class Meta(
        @SerialName("api")
        var api: String = "",
        @SerialName("common")
        var common: Common = Common(),
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
        var minHour: Int = 0,
        @SerialName("rate")
        var rate: Double = 0.0
    )

}