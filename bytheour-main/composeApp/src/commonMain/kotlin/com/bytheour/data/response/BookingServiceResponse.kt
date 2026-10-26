package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookingServiceResponse(
    @SerialName("data")
    var data: Data = Data(),

    @SerialName("meta")
    var meta: Meta = Meta()
) {

    @Serializable
    data class Data(
        @SerialName("booking")
        var booking: Booking = Booking(),

        @SerialName("additional")
        var additional: Additional = Additional(),

        @SerialName("feedback")
        var feedback: String? = null,

        @SerialName("service")
        var service: Service = Service(),

        @SerialName("address")
        var address: Address = Address(),

        @SerialName("customer")
        var customer: Customer = Customer(),

        @SerialName("flags")
        var flags: Flags = Flags(),

        @SerialName("times")
        var times: List<String> = emptyList()
    )

    @Serializable
    data class Booking(
        @SerialName("id")
        var id: String = "",

        @SerialName("date")
        var date: String = "",

        @SerialName("time")
        var time: String = "",

        @SerialName("hours")
        var hours: String? = null,

        @SerialName("completion_minutes")
        var completionMinutes: String? = null,

        @SerialName("user_time")
        var userTime: String? = null
    )

    @Serializable
    data class Additional(
        @SerialName("hours")
        var hours: String? = null
    )

    @Serializable
    data class Service(
        @SerialName("id")
        var id: String = "",

        @SerialName("title")
        var title: String = "",

        @SerialName("description")
        var description: String = "",

        @SerialName("image")
        var image: String = "",

        @SerialName("category")
        var category: Category = Category(),

        @SerialName("rate")
        var inspectionRate :Double = 0.0

    )

    @Serializable
    data class Category(
        @SerialName("id")
        var id: String = "",

        @SerialName("title")
        var title: String = ""
    )

    @Serializable
    data class Address(
        @SerialName("id")
        var id: String = "",

        @SerialName("name")
        var name: String = "",

        @SerialName("house")
        var house: String = "",

        @SerialName("address")
        var address1: SubAddress = SubAddress(),

        @SerialName("lat")
        var lat: String = "",

        @SerialName("lng")
        var lng: String = "",

        @SerialName("default")
        var default: Boolean = false
    )

    @Serializable
    data class SubAddress(
        @SerialName("line_1")
        var line1: String = "",

        @SerialName("line_2")
        var line2: String = "",

        @SerialName("city")
        var city: String = "",

        @SerialName("state")
        var state: String = "",

        @SerialName("zip")
        var zip: String = ""
    ) {
        val fullAdd: String
            get() = "$line1, $line2 $city - $zip $state"
    }

    @Serializable
    data class Customer(
        @SerialName("id")
        var id: String = "",

        @SerialName("first_name")
        var firstName: String = "",

        @SerialName("last_name")
        var lastName: String = "",

        @SerialName("username")
        var username: String = "",

        @SerialName("profile")
        var profile: String = "",

        @SerialName("portfolio")
        var portfolio: Portfolio = Portfolio(),

        @SerialName("contact")
        var contact: Contact = Contact()
    )

    @Serializable
    data class Portfolio(
        @SerialName("complted") // This is a typo from backend
        var completed: String = "",

        @SerialName("ratings")
        var ratings: String = "",

        @SerialName("type")
        var type: String = ""
    )

    @Serializable
    data class Contact(
        @SerialName("code")
        var code: String = "",

        @SerialName("number")
        var number: String = ""
    )

    @Serializable
    data class Flags(
        @SerialName("type")
        var type: String = "",

        @SerialName("cancelled")
        var cancelled: Boolean = false,

        @SerialName("accepted")
        var accepted: Boolean = false,

        @SerialName("decline")
        var decline: Boolean = false,

        @SerialName("completed")
        var completed: Boolean = false,

        @SerialName("paid")
        var paid: Boolean = false,

        @SerialName("extra_hours")
        var extraHours: Boolean = false,

        @SerialName("is_reschedule_allowed")
        var isRescheduleAllowed: Boolean = false,

        @SerialName("is_quotation_sent")
        var isQuotationSent: Boolean = false,

        @SerialName("is_quotation_accepted")
        var isQuotationAccepted: Boolean = false,

        @SerialName("is_user_opted_other_time")
        var isUserOptedOtherTime: Boolean = false,

        @SerialName("is_payment_confirmed")
        var isPaymentConfirmed: Boolean = false,

        @SerialName("started_by_worker")
        var startedByWorker: Boolean = false,

        @SerialName("started_by_user")
        var startedByUser: Boolean = false,

        @SerialName("ended_by_worker")
        var endedByWorker: Boolean = false,

        @SerialName("ended_by_user")
        var endedByUser: Boolean = false,

        @SerialName("user_opt_fixed_price")
        var userOptFixedPrice: Boolean = false,

        @SerialName("no_processing_fee")
        var noProcessingFee: Boolean = false,

        @SerialName("service_at_premises")
        var serviceAtPremises: String = "",

        @SerialName("cancel_after_start_time")
        var cancelAfterStartTime: String? = null
    )
}
