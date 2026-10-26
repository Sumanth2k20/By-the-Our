package com.bytheour.data.response



import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetServiceProviderDetailsResponse(
    @SerialName("data")
    var data: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("cost")
        var cost: Cost = Cost(),
        @SerialName("description")
        var description: String = "",
        @SerialName("services")
        var services: ArrayList<Service> = arrayListOf(),
        @SerialName("images")
        var images: ArrayList<String> = arrayListOf(),
        @SerialName("worker")
        var worker: Worker = Worker()
    )

    @Serializable
    data class Cost(
        @SerialName("rate")
        var rate: Int = 0,
        @SerialName("min_hour")
        var minHour: Int = 0
    )

    @Serializable
    data class Service(
        @SerialName("id")
        var id: String = "",
        @SerialName("rate")
        var rate: Int = 0,
        @SerialName("min_hour")
        var minHour: Int = 0,
        @SerialName("category")
        var category: Category = Category(),
        @SerialName("service")
        var service: ServiceDetails = ServiceDetails(),
        @SerialName("portfolio")
        var portfolio: ServicePortfolio = ServicePortfolio(),
        @SerialName("experience")
        var experience: Int = 0,
        @SerialName("service_at_premises")
        var serviceAtPremises: String = "",
        @SerialName("description")
        var description: String = "",
        @SerialName("radius")
        var radius: Int = 0
    )

    @Serializable
    data class Category(
        @SerialName("id")
        var id: String = "",
        @SerialName("title")
        var title: String = ""
    )

    @Serializable
    data class ServiceDetails(
        @SerialName("id")
        var id: String = "",
        @SerialName("title")
        var title: String = "",
        @SerialName("description")
        var description: String = "",
        @SerialName("image")
        var image: String = ""
    )

    @Serializable
    data class ServicePortfolio(
        @SerialName("service_ratings")
        var serviceRatings: String = "",
        @SerialName("service_rating_text")
        var serviceRatingText: String = ""
    )

    @Serializable
    data class Worker(
        @SerialName("id")
        var id: String = "",
        @SerialName("first_name")
        var firstName: String = "",
        @SerialName("last_name")
        var lastName: String = "",
        @SerialName("username")
        var username: String = "",
        @SerialName("verification_status")
        var verificationStatus: String = "",
        @SerialName("experience")
        var experience: Int = 0,
        @SerialName("service_at_premises")
        var serviceAtPremises: String = "",
        @SerialName("about")
        var about: String = "",
        @SerialName("contact")
        var contact: Contact = Contact(),
        @SerialName("portfolio")
        var portfolio: WorkerPortfolio = WorkerPortfolio(),
        @SerialName("profile")
        var profile: String = "",
        @SerialName("videos")
        var videos: ArrayList<String> = arrayListOf(),
        @SerialName("work_area")
        var workArea: WorkArea = WorkArea(),
        @SerialName("tax_classification")
        var taxClassification: TaxClassification = TaxClassification()
    )

    @Serializable
    data class Contact(
        @SerialName("code")
        var code: String = "",
        @SerialName("number")
        var number: String = ""
    )

    @Serializable
    data class WorkerPortfolio(
        @SerialName("complted")
        var completed: String = "",
        @SerialName("ratings")
        var ratings: String = "",
        @SerialName("type")
        var type: String = "",
        @SerialName("service_ratings")
        var serviceRatings: String = "",
        @SerialName("service_rating_text")
        var serviceRatingText: String = ""
    )

    @Serializable
    data class WorkArea(
        @SerialName("selectedServiceRadius")
        var selectedServiceRadius: Int = 0,
        @SerialName("radius")
        var radius: Int = 0,
        @SerialName("lat")
        var lat: String = "",
        @SerialName("lng")
        var lng: String = "",
        @SerialName("distance")
        var distance: String = ""
    )

    @Serializable
    data class TaxClassification(
        @SerialName("id")
        var id: String = "",
        @SerialName("type")
        var type: String = ""
    )

    @Serializable
    data class Meta(
        @SerialName("url")
        var url: String = "",
        @SerialName("api")
        var api: String = "",
        @SerialName("message")
        var message: String = ""
    )
}