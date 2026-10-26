package com.bytheour.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AcceptInspectionReportResponse(
    @SerialName("data")
    val data: InspectionReportData? = null,
    @SerialName("meta")
    val meta: Meta? = null
)

@Serializable
data class InspectionReportData(
    @SerialName("booking")
    val booking: InspectionBooking? = null,
    @SerialName("additional")
    val additional: Additional? = null,
    @SerialName("review")
    val review: String? = null,
    @SerialName("feedback")
    val feedback: String? = null,
    @SerialName("service")
    val service: InspectionService? = null,
    @SerialName("address")
    val address: Address? = null,
    @SerialName("worker_address")
    val workerAddress: String? = null,
    @SerialName("cancellation")
    val cancellation: String? = null,
    @SerialName("cost")
    val cost: Cost? = null,
    @SerialName("times")
    val times: List<String>? = null,
    @SerialName("worker")
    val worker: Worker? = null,
    @SerialName("customer")
    val customer: Customer? = null,
    @SerialName("bill")
    val bill: Bill? = null,
    @SerialName("flags")
    val flags: Flags? = null,
    @SerialName("mediation")
    val mediation: Mediation? = null,
    @SerialName("support")
    val support: Support? = null,
    @SerialName("inspection_report")
    val inspectionReport: String? = null
)

@Serializable
data class InspectionBooking(
    @SerialName("id")
    val id: String? = null,
    @SerialName("date")
    val date: String? = null,
    @SerialName("time")
    val time: String? = null,
    @SerialName("hours")
    val hours: Int? = null,
    @SerialName("completion_minutes")
    val completionMinutes: String? = null,
    @SerialName("user_time")
    val userTime: String? = null
)

@Serializable
data class Additional(
    @SerialName("hours")
    val hours: String? = null
)

@Serializable
data class InspectionService(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String? = null,
    @SerialName("description")
    val description: String? = null,
    @SerialName("is_inspection")
    val isInspection: String? = null,
    @SerialName("rate")
    val rate: Double? = null,
    @SerialName("image")
    val image: String? = null,
    @SerialName("category")
    val category: InspectionCategory? = null
)

@Serializable
data class InspectionCategory(
    @SerialName("id")
    val id: String? = null,
    @SerialName("title")
    val title: String? = null
)

@Serializable
data class Address(
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
data class Cost(
    @SerialName("rate")
    val rate: Int? = null,
    @SerialName("min_hour")
    val minHour: Int? = null
)

@Serializable
data class Worker(
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
    @SerialName("about")
    val about: String? = null,
    @SerialName("contact")
    val contact: Contact? = null,
    @SerialName("portfolio")
    val portfolio: WorkerPortfolio? = null,
    @SerialName("work_area")
    val workArea: WorkArea? = null,
    @SerialName("tax_classification")
    val taxClassification: TaxClassification? = null,
    @SerialName("verification_status")
    val verificationStatus: String? = null,
    @SerialName("experience")
    val experience: Int? = null
)

@Serializable
data class Contact(
    @SerialName("code")
    val code: String? = null,
    @SerialName("number")
    val number: String? = null
)

@Serializable
data class WorkerPortfolio(
    @SerialName("complted")
    val complted: String? = null,
    @SerialName("ratings")
    val ratings: String? = null,
    @SerialName("service_ratings")
    val serviceRatings: String? = null,
    @SerialName("service_rating_text")
    val serviceRatingText: String? = null,
    @SerialName("type")
    val type: String? = null
)

@Serializable
data class WorkArea(
    @SerialName("radius")
    val radius: Int? = null,
    @SerialName("lat")
    val lat: String? = null,
    @SerialName("lng")
    val lng: String? = null
)

@Serializable
data class TaxClassification(
    @SerialName("id")
    val id: String? = null,
    @SerialName("type")
    val type: String? = null
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
    val portfolio: CustomerPortfolio? = null,
    @SerialName("contact")
    val contact: Contact? = null
)

@Serializable
data class CustomerPortfolio(
    @SerialName("complted")
    val complted: String? = null,
    @SerialName("ratings")
    val ratings: String? = null,
    @SerialName("type")
    val type: String? = null
)

@Serializable
data class Bill(
    @SerialName("price_type")
    val priceType: String? = null,
    @SerialName("total")
    val total: Int? = null,
    @SerialName("amount")
    val amount: String? = null,
    @SerialName("wallet_amount")
    val walletAmount: Int? = null,
    @SerialName("tax")
    val tax: Tax? = null,
    @SerialName("promo")
    val promo: Promo? = null,
    @SerialName("commission")
    val commission: Commission? = null,
    @SerialName("payment")
    val payment: Payment? = null,
    @SerialName("refund_percentage")
    val refundPercentage: String? = null
)

@Serializable
data class Tax(
    @SerialName("percentage")
    val percentage: Int? = null,
    @SerialName("amount")
    val amount: Int? = null
)

@Serializable
data class Promo(
    @SerialName("code")
    val code: String? = null,
    @SerialName("amount")
    val amount: Int? = null
)

@Serializable
data class Commission(
    @SerialName("percentage")
    val percentage: Int? = null,
    @SerialName("amount")
    val amount: Int? = null,
    @SerialName("type")
    val type: String? = null
)

@Serializable
data class Payment(
    @SerialName("mode")
    val mode: String? = null
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
data class Mediation(
    @SerialName("amount_processed")
    val amountProcessed: String? = null,
    @SerialName("mediation_raised")
    val mediationRaised: String? = null,
    @SerialName("mediation_user_reason")
    val mediationUserReason: String? = null,
    @SerialName("mediation_amount")
    val mediationAmount: String? = null,
    @SerialName("mediation_status")
    val mediationStatus: String? = null,
    @SerialName("mediation_worker_reason")
    val mediationWorkerReason: String? = null,
    @SerialName("mediation_accept_amount")
    val mediationAcceptAmount: String? = null
)

@Serializable
data class Support(
    @SerialName("create_new")
    val createNew: Boolean? = null,
    @SerialName("already_created")
    val alreadyCreated: Boolean? = null,
    @SerialName("ticket")
    val ticket: String? = null
)

