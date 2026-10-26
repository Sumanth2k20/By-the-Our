package com.bytheour.data.model.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelService(
    @SerialName("data")
    var `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta(),
    @SerialName("gallery_bookings")
    var galleryBookings: ArrayList<GalleryBooking> = arrayListOf(),
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

        ){
        val formatMinHrs: String
            get() {
                return if (minHour > 1) "$minHour Hrs" else "$minHour Hr"
            }
        val formatRate: String
            get() {
                return String.toString()
            }
    }


    @Serializable
    data class GalleryBooking(
        @SerialName("custom_id")
        val customId: String = "",
        @SerialName("gallery_plan_id")
        val galleryPlanId: Int = 0,
        @SerialName("no_of_pictures")
        val noOfPictures: Int = 0,
        @SerialName("no_of_pictures_used")
        val noOfPicturesUsed: Int = 0,
        @SerialName("is_unlimited")
        val isUnlimited: String = "n",
        @SerialName("start_date")
        val startDate: String = "",
        @SerialName("end_date")
        val endDate: String = "",
        @SerialName("amount")
        val amount: Double = 0.0
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