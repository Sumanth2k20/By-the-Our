package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetWorkerBookingRequest(
    @SerialName("data")
    val data: Data? = null,
    @SerialName("meta")
    val meta: Meta? = null
) {
    @Serializable
    data class Data(
        @SerialName("booking")
        val booking: Booking? = null,
        @SerialName("additional")
        val additional: Additional? = null,
        @SerialName("feedback")
        val feedback: String? = null,
        @SerialName("service")
        val service: Service? = null,
        @SerialName("address")
        val address: AddressWrapper? = null,
        @SerialName("times")
        val times: List<TimeSlot> = emptyList(),
        @SerialName("customer")
        val customer: Customer? = null,
        @SerialName("flags")
        val flags: Flags? = null,
        @SerialName("inspection_report")
        val inspectionReport: InspectionReport? = null
    )
    @Serializable
    data class TimeSlot(
        @SerialName("id")
        val id: Int? = null,
        @SerialName("booking_id")
        val bookingId: Int? = null,
        @SerialName("start_time")
        val startTime: String? = null,
        @SerialName("end_time")
        val endTime: String? = null,
        @SerialName("deleted_at")
        val deletedAt: String? = null,
        @SerialName("created_at")
        val createdAt: String? = null,
        @SerialName("updated_at")
        val updatedAt: String? = null
    )

    @Serializable
    data class Booking(
        @SerialName("id")
        val id: String? = null,
        @SerialName("date")
        val date: String? = null,
        @SerialName("time")
        val time: String? = null,
        @SerialName("hours")
        val hours: Int? = null,
        @SerialName("completion_minutes")
        val completionMinutes: Int? = null,
        @SerialName("user_time")
        val userTime: String? = null,
        @SerialName("inspection_amount")
        val inspectionAmount: Double? = null,
    )

    @Serializable
    data class Additional(
        @SerialName("hours")
        val hours: Int? = null
    )

    @Serializable
    data class Service(
        @SerialName("id")
        val id: String? = null,
        @SerialName("title")
        val title: String? = null,
        @SerialName("description")
        val description: String? = null,
        @SerialName("image")
        val image: String? = null,
        @SerialName("category")
        val category: Category? = null,
        @SerialName("rate")
        val inspectionRate: String? = null
    )

    @Serializable
    data class Category(
        @SerialName("id")
        val id: String? = null,
        @SerialName("title")
        val title: String? = null
    )

    @Serializable
    data class AddressWrapper(
        @SerialName("id")
        val id: String? = null,
        @SerialName("name")
        val name: String? = null,
        @SerialName("house")
        val house: String? = null,
        @SerialName("address")
        val address: AddressDetail? = null,
        @SerialName("lat")
        val lat: String? = null,
        @SerialName("lng")
        val lng: String? = null,
        @SerialName("default")
        val default: Boolean? = null
    )

    @Serializable
    data class AddressDetail(
        @SerialName("line_1")
        val line1: String? = null,
        @SerialName("line_2")
        val line2: String? = null,
        @SerialName("city")
        val city: String? = null,
        @SerialName("state")
        val state: String? = null,
        @SerialName("zip")
        val zip: String? = null
    )

    @Serializable
    data class Customer(
        @SerialName("id")
        val id: String? = null,
        @SerialName("first_name")
        val firstName: String? = null,
        @SerialName("last_name")
        val lastName: String? = null,
        @SerialName("username")
        val username: String? = null,
        @SerialName("profile")
        val profile: String? = null,
        @SerialName("portfolio")
        val portfolio: Portfolio? = null,
        @SerialName("contact")
        val contact: Contact? = null
    )

    @Serializable
    data class Portfolio(
        @SerialName("complted")
        val completed: String? = null,
        @SerialName("ratings")
        val ratings: String? = null,
        @SerialName("type")
        val type: String? = null
    )

    @Serializable
    data class Contact(
        @SerialName("code")
        val code: String? = null,
        @SerialName("number")
        val number: String? = null
    )

    @Serializable
    data class Flags(
        @SerialName("type")
        val type: String? = null,
        @SerialName("cancelled")
        val cancelled: Boolean? = null,
        @SerialName("accepted")
        val accepted: Boolean? = null,
        @SerialName("decline")
        val decline: Boolean? = null,
        @SerialName("completed")
        val completed: Boolean? = null,
        @SerialName("paid")
        val paid: Boolean? = null,
        @SerialName("extra_hours")
        val extraHours: Boolean? = null,
        @SerialName("is_reschedule_allowed")
        val isRescheduleAllowed: Boolean? = null,
        @SerialName("is_quotation_sent")
        val isQuotationSent: Boolean? = null,
        @SerialName("is_quotation_accepted")
        val isQuotationAccepted: Boolean? = null,
        @SerialName("is_user_opted_other_time")
        val isUserOptedOtherTime: Boolean? = null,
        @SerialName("is_payment_confirmed")
        val isPaymentConfirmed: Boolean? = null,
        @SerialName("started_by_worker")
        val startedByWorker: Boolean? = null,
        @SerialName("started_by_user")
        val startedByUser: Boolean? = null,
        @SerialName("ended_by_worker")
        val endedByWorker: Boolean? = null,
        @SerialName("ended_by_user")
        val endedByUser: Boolean? = null,
        @SerialName("user_opt_fixed_price")
        val userOptFixedPrice: Boolean? = null,
        @SerialName("no_processing_fee")
        val noProcessingFee: Boolean? = null,
        @SerialName("service_at_premises")
        val serviceAtPremises: String? = null,
        @SerialName("cancel_after_start_time")
        val cancelAfterStartTime: String? = null,
        @SerialName("is_inspection")
        val isInspection: Boolean? = null,
        @SerialName("inspection_after_booking")
        val inspectionAfterBooking: Boolean? = null
    )

    @Serializable
    data class InspectionReport(
        @SerialName("id")
        val id: String? = null,
        @SerialName("problem")
        val problem: String? = null,
        @SerialName("problem_description")
        val problemDescription: String? = null,
        @SerialName("problem_solution")
        val problemSolution: String? = null,
        @SerialName("estimation")
        val estimation: Double? = null,
        @SerialName("available_date_time")
        val availableDateTime: String? = null,
        @SerialName("status")
        val status: String? = null,
        @SerialName("reject_reason")
        val rejectReason: String? = null
    )
    @Serializable
    data class Meta(
        @SerialName("api")
        val api: String? = null,
        @SerialName("message")
        val message: String? = null,
        @SerialName("url")
        val url: String? = null,
        @SerialName("unread_count")
        val unreadCount: Int? = null,
        @SerialName("amount")
        val amount: Amount? = null,
        @SerialName("count")
        val count: Int? = null
    ) {
        @Serializable
        data class Amount(
            @SerialName("wallet")
            val wallet: Double? = 0.0,
            @SerialName("hold")
            val hold: Double? = 0.0,
            @SerialName("process")
            val process: Double? = 0.0
        )
    }
}
