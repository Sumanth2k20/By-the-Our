package com.bytheour.data.model.response


import com.bytheour.data.Meta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModelProviders(
    @SerialName("data")
    var `data`: ArrayList<Data> = arrayListOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("cost")
        var cost: Cost = Cost(),
        @SerialName("times")
        var `times`: List<Times> = listOf(),
        @SerialName("worker")
        var worker: Worker = Worker(),
        @SerialName("services")
        var services: ArrayList<ModelService.Data> = arrayListOf(),
        @SerialName("description")
        var description: String? = "",
        @SerialName("images")
        val images : ArrayList<ServerImages>?= null,
    )

    @Serializable
    data class Worker(
        @SerialName("contact")
        var contact: Contact = Contact(),
        @SerialName("experience")
        var experience: Float = 0f,
        @SerialName("service_at_premises")
        var serviceAtPremises: String = "n",
        @SerialName("verification_status")
        var verificationStatus: String? = null,
        @SerialName("radius")
        var radius: Float = 0f,
        @SerialName("first_name")
        var firstName: String = "",
        @SerialName("description")
        var description: String = "",
        @SerialName("about")
        var about: String? = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("last_name")
        var lastName: String = "",
        @SerialName("username")
        var username: String? = null,
        @SerialName("portfolio")
        var portfolio: Portfolio = Portfolio(),
        @SerialName("profile")
        var profile: String = "",
        @SerialName("work_area")
        var workArea: WorkArea = WorkArea(),
        @SerialName("videos")
        var videos: ArrayList<Video> = arrayListOf(),
        @SerialName("tax_classification")
        var taxClassification: ModelUser.TaxClassification? = null
    )  {
        val formatExp: String
            get() {
                return "$experience " + (if (experience > 1) "Years" else "Year")
            }
        val formatExpCard: String
            get() {
                return "Exp-$experience " + (if (experience > 1.0) "Yrs" else "Yr")
            }
    }

    @Serializable
    data class Contact(
        @SerialName("code")
        var code: String = "",
        @SerialName("number")
        var number: String = ""
    ) 

    @Serializable
    data class WorkArea(
        @SerialName("radius")
        val radius: Float = 0f,
        @SerialName("lat")
        val lat: String = "",
        @SerialName("lng")
        val lng: String = "",
        @SerialName("distance")
        var distance: String = "",
        @SerialName("selectedServiceRadius")
        var selectedServiceRadius: Float = 0f
    ) 

    @Serializable
    data class Portfolio(
        @SerialName("complted")
        var complted: String = "",
        @SerialName("ratings")
        var ratings: Float = 0f,
        @SerialName("service_ratings")
        var serviceRating: Float = 0f,
        @SerialName("service_rating_text")
        var serviceRatingText: String = "",
        @SerialName("type")
        var type: String = ""
    ) 

    @Serializable
    data class Video(
        @SerialName("file")
        var `file`: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("name")
        var name: String = ""
    ) 

}

@Serializable
data class Cost(
    @SerialName("min_hour")
    var minHour: Int? = 0,
    @SerialName("rate")
    var rate: Float = 0f
)  {
    val formatMinHr: String
        get() {
            return "${minHour ?: 0} " + (if ((minHour ?: 0) > 1) "Hrs" else "Hr")
        }

    val totalCost : Double
        get() {
            return (rate * (minHour ?:0)).toDouble()
        }
}
@Serializable
data class Times(
    @SerialName("id")
    var id: Int? = 0,
    @SerialName("booking_id")
    var bookingId: Int? = 0,
    @SerialName("start_time")
    var startTime: String? = "",
    @SerialName("end_time")
    var endTime: String? = "",
    @SerialName("deleted_at")
    var deletedAt: String? = null,
    @SerialName("created_at")
    var createdAt: String? = "",
    @SerialName("updated_at")
    var updatedAt: String? = ""
) 