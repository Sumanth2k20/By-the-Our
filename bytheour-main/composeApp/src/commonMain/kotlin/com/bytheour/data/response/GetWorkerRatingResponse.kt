package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class GetWorkerRatingResponse(
    @SerialName("data")
    val `data`: List<Data> = listOf(),
    @SerialName("meta")
    val meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("date")
        val date: String = "",
        @SerialName("id")
        val id: String = "",
        @SerialName("rating")
        val rating: Int = 0,
        @SerialName("review")
        val review: String? = "",
        @SerialName("allow_to_modity")
        val allowToModify: Boolean = false,
        @SerialName("service")
        val service: Service = Service(),
        @SerialName("type")
        val type: String = "", // Excellent
        @SerialName("user")
        val user: User = User(),
        @SerialName("worker")
        val worker: Worker = Worker(),
        @SerialName("booking")
        val booking: Booking = Booking(),
        @SerialName("given")
        val given:Given? = null,
        @SerialName("taken")
        val taken: Taken? = null,
    ) {

        @Serializable
        data class Taken(
            @SerialName("allow_to_modity")
            val allowToModity: Boolean, // false
            @SerialName("date")
            val date: String, // 01 Mar, 2023
            @SerialName("id")
            val id: String, //
            @SerialName("rating")
            val rating: Int, // 2
            @SerialName("service_rating")
            val serviceRating: Int?=null, // 2
            @SerialName("service_rating_text")
            val serviceRatingText: String?, // 2
            @SerialName("review")
            val review: String?=null,
            @SerialName("type")
            val type: String // Poor
        )

        @Serializable
        data class Given(
            @SerialName("allow_to_modity")
            val allowToModity: Boolean, // true
            @SerialName("date")
            val date: String, // 15 Mar, 2023
            @SerialName("id")
            val id: String, //
            @SerialName("service_rating")
            val serviceRating: Int? = 0, // 5
            @SerialName("service_rating_text")
            val serviceRatingText: String?, // 5
            @SerialName("rating")
            val rating: Int?, // 5
            @SerialName("review")
            val review: String? = "", // Excellent work
            @SerialName("type")
            val type: String // Excellent
        )

        @Serializable
        data class Service(
            @SerialName("id")
            val id: String = "", // BBA87M1gPx96MEy0eUre
            @SerialName("title")
            val title: String = "" // Split AC Cleaning
        )

        @Serializable
        data class User(
            @SerialName("first_name")
            val firstName: String = "", // bella
            @SerialName("id")
            val id: String = "", // AdTtz5XEfaXoVxa8iQPl
            @SerialName("last_name")
            val lastName: String = "", // Swan
            @SerialName("profile")
            val profile: String = "" // https://bythehour.s3.ap-south-1.amazonaws.com/default/user.png
        )

        @Serializable
        data class Worker(
            @SerialName("first_name")
            val firstName: String = "", // John
            @SerialName("id")
            val id: String = "", // XiuG3tDuGLx4ZP6IzV7k
            @SerialName("last_name")
            val lastName: String = "", // Day
            @SerialName("profile")
            val profile: String = "" // https://bythehour.s3.ap-south-1.amazonaws.com/default/worker.png
        )

        @Serializable
        data class Booking(
            @SerialName("date")
            val date: String = "", // John
            @SerialName("id")
            val id: String = "", // XiuG3tDuGLx4ZP6IzV7k
            @SerialName("time")
            val time: String = "", // Day
            @SerialName("hours")
            val hours: Int = 0 // https://bythehour.s3.ap-south-1.amazonaws.com/default/worker.png
        )
    }
}
