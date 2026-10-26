package com.bytheour.data.response

import com.bytheour.data.Meta
import com.bytheour.data.model.response.Cost
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.model.response.ModelProviders
import com.bytheour.data.model.response.Times
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient


@Serializable
data class ModelProviderBookingList(
    @SerialName("data")
    var `data`: List<Data> = listOf(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("feedback")
        var feedback: Feedback? = Feedback(),
        @SerialName("address")
        var address: Address? = Address(),
        @SerialName("worker_address")
        var workerAddress: WorkerAddress? = WorkerAddress(),
        @SerialName("bill")
        var bill: Bill = Bill(),
        @SerialName("booking")
        var booking: Booking = Booking(),
        @SerialName("customer")
        var customer: Customer = Customer(),
        @SerialName("flags")
        var flags: Flags = Flags(),
        @SerialName("service")
        var service: Service = Service(),
        @SerialName("worker")
        var worker: Worker = Worker(),
        @SerialName("additional")
        var additional: Additional = Additional(),
        @SerialName("review")
        var review: ModelGetRating.Data? = ModelGetRating.Data(),
        @SerialName("cancellation")
        var cancellation: Cancellation? = Cancellation(),
        @SerialName("mediation")
        var mediation: Mediation? = Mediation(),
        @SerialName("support")
        var support: Support? = Support(),
        @SerialName("cost")
        var cost: Cost = Cost(),
        @SerialName("times")
        var times: List<Times> = listOf(),
        @Transient
        var distance : String = "",
        @Transient
        var providerCancelledMessage : String = "",
        @SerialName("inspection_report")
        val inspectionReport :InspectionReport? = InspectionReport()

    )

    @Serializable
    data class InspectionReport(
        @SerialName("id")
        var id: String = "",
        @SerialName("problem")
        var problem: String = "",
        @SerialName("problem_description")
        var problemDescription: String = "",
        @SerialName("problem_solution")
        var problemSolution: String = "",
        @SerialName("estimation")
        var inspectionEstimation: String = "",
        @SerialName("available_date_time")
        var availableTime: String = "",
    )

    @Serializable
    data class Cancellation(
        @SerialName("id")
        var id: String = "",
        @SerialName("reason")
        var reason: String = "",
        @SerialName("reason_id")
        var reasonId: String = "",
        @SerialName("cancelled_by")
        var cancelledBy: String = "",
        @SerialName("refund_date")
        var refundDate: String = "",

    )

    @Serializable
    data class Additional(
        @SerialName("hours")
        var hours: Hours? = Hours()
    ) 

    @Serializable
    data class Hours(
        @SerialName("amount")
        var amount: Double = 0.0,
        @SerialName("approved")
        var approved: Boolean = false,
        @SerialName("code")
        var code: String = "",
        @SerialName("hours")
        var hours: Double = 0.0
    )  {
        val formatMinHrs: String
            get() {
                return if (hours > 1) "$hours Hrs" else "$hours Hr"
            }
    }

    @Serializable
    data class Address(
        @SerialName("address")
        var address: SubAddress = SubAddress(),
        @SerialName("default")
        var default: Boolean = false,
        @SerialName("house")
        var house: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("lat")
        var lat: String = "",
        @SerialName("lng")
        var lng: String = "",
        @SerialName("name")
        var name: String = ""
    ) 

    @Serializable
    data class WorkerAddress(
        @SerialName("address")
        var address: SubAddress = SubAddress(),
        @SerialName("default")
        var default: Boolean = false,
        @SerialName("house")
        var house: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("lat")
        var lat: String = "",
        @SerialName("lng")
        var lng: String = "",
        @SerialName("name")
        var name: String = ""
    )

    @Serializable
    data class SubAddress(
        @SerialName("city")
        var city: String = "",
        @SerialName("line_1")
        var line1: String = "",
        @SerialName("line_2")
        var line2: String = "",
        @SerialName("state")
        var state: String = "",
        @SerialName("zip")
        var zip: String = ""
    )  {
        val fullAdd: String
            get() {
                val parts = listOf(line1, line2, city, zip, state)
                    .filter { it.isNotBlank() }
                return parts.joinToString(separator = " ") { it.trim() }
            }
    }

    @Serializable
    data class Bill(
        @SerialName("price_type")
        val priceType : String = "",
        @SerialName("amount")
        var amount: Double = 0.0,
        @SerialName("commission")
        var commission: Commission = Commission(),
        @SerialName("promo")
        var promo: Promo = Promo(),
        @SerialName("tax")
        var tax: Tax = Tax(),
        @SerialName("total")
        var total: Double = 0.0,
        @SerialName("wallet_amount")
        var walletAmount: Double = 0.0,
        @SerialName("payment")
        var payment: Payment = Payment(),
        @SerialName("refund_percentage")
        var refundPercentage: String?=null,
        @SerialName("inspection_amount")
        var inspectionAmount: Double = 0.0,
    )  {

        @Serializable
        data class Payment(
            @SerialName("mode")
            var mode: String? = "",
        ) 

        @Serializable
        data class Commission(
            @SerialName("amount")
            var percentageAmount: Double = 0.0,
            @SerialName("percentage")
            var percentage: Double = 0.0,
            @SerialName("type")
            var type: String = "",
        ) 

        @Serializable
        data class Promo(
            @SerialName("amount")
            var amount: Double = 0.0,
            @SerialName("code")
            var code: String = ""
        ) 

        @Serializable
        data class Tax(
            @SerialName("amount")
            var amount: Double = 0.0,
            @SerialName("percentage")
            var percentage: Double = 0.0
        ) 
    }

    @Serializable
    data class Booking(
        @SerialName("date")
        var date: String = "",
        @SerialName("id")
        var id: String = "",
        @SerialName("time")
        var time: String = "",
        @SerialName("hours")
        var hours: Double = 0.0,
        @SerialName("user_time")
        var userTime: String? = null,
        @SerialName("completion_minutes")
        var completionMinutes: String? = null
    )

    @Serializable
    data class Customer(
        @SerialName("id")
        var id: String = "",
        @SerialName("contact")
        var contact: Contact = Contact(),
        @SerialName("first_name")
        var firstName: String = "",
        @SerialName("last_name")
        var lastName: String = "",
        @SerialName("profile")
        var profile: String = "",
        @SerialName("portfolio")
        var portfolio: Portfolio? = Portfolio(),
    )  {

        val fullName: String
            get() {
                return "$firstName $lastName"
            }

        @Serializable
        data class Contact(
            @SerialName("code")
            var code: String = "",
            @SerialName("number")
            var number: String = ""
        )

        @Serializable
        data class Portfolio(
            @SerialName("complted") // assuming this is a typo from backend; if it's "completed", update the key
            var completed: String = "",
            @SerialName("ratings")
            var ratings: String = "",
            @SerialName("type")
            var type: String = ""
        )

    }

    @Serializable
    data class Flags(
        @SerialName("type")
        var type: String = "",
        @SerialName("completed")
        var completed: Boolean = false,
        @SerialName("extra_hours")
        var extraHours: Boolean = false,
        @SerialName("accepted")
        var accepted: Boolean = false,
        @SerialName("cancelled")
        var cancelled: Boolean = false,
        @SerialName("decline")
        var decline: Boolean = false,
        @SerialName("paid")
        var paid: Boolean = false,
        @SerialName("is_reschedule_allowed")
        var isRescheduleAllowed: Boolean = false,
        @SerialName("is_quotation_sent")
        var isQuotationSent: Boolean = false,
        @SerialName("is_quotation_accepted")
        var isQuotationAccepted: Boolean = false,
        @SerialName("is_payment_confirmed")
        var isPaymentConfirmed: Boolean = false,
        @SerialName("started_by_worker")
        var jobStartedByWorker: Boolean = false,
        @SerialName("started_by_user")
        var jobStartedByUser: Boolean = false,
        @SerialName("ended_by_worker")
        var jobEndedByWorker: Boolean = false,
        @SerialName("ended_by_user")
        var jobEndedByUser: Boolean = false,
        @SerialName("user_opt_fixed_price")
        var userOptFixedPrice: Boolean = false,
        @SerialName("is_user_opted_other_time")
        var isUserOptedOtherTime: Boolean = false,
        @SerialName("no_processing_fee")
        var noProcessingFee: Boolean = false,
        @SerialName("service_at_premises")
        var serviceAtPremises: String = "",
        @SerialName("cancel_after_start_time")
        var cancelAfterStartTime: String = "n",
        @SerialName("is_inspection")
        val isInspection :Boolean = false
    )

    @Serializable
    data class Mediation(
        @SerialName("amount_processed")
        var amountProcessed: String = "",
        @SerialName("mediation_raised")
        var mediationRaised: String = "",
        @SerialName("mediation_user_reason")
        var mediationUserReason: String? = null,
        @SerialName("mediation_amount")
        var mediationAmount: String? = null,
        @SerialName("mediation_status")
        var mediationStatus: String = "",
        @SerialName("mediation_worker_reason")
        var mediationWorkerReason: String? = null,
        @SerialName("mediation_accept_amount")
        var mediationAcceptAmount:String? = null
    )  {

        val mediationAcceptAmountMinusTenPercent: String
            get() {
                return if (mediationAcceptAmount != null) {
                    val amount = mediationAcceptAmount!!.toDoubleOrNull()
                    if (amount != null) {
                        val adjustedAmount = amount * 0.9
                        val roundedAmount = if (adjustedAmount - adjustedAmount.toInt() <= 0.5) {
                            adjustedAmount.toInt()
                        } else {
                            adjustedAmount.toInt() + 1
                        }
                        String.toString()
                    } else {
                        "Invalid amount"
                    }
                } else {
                    "No amount"
                }
            }

        val mediationUserReasonString: String
            get() {
                val reason = mediationUserReason ?: ""
                return if (reason.length > 45) {
                    reason.substring(0, 40) + "..."
                } else {
                    reason
                }
            }
        val mediationWorkerReasonString: String
            get() {
                val reason = mediationWorkerReason ?: ""
                return if (reason.length > 45) {
                    reason.substring(0, 40) + "..."
                } else {
                    reason
                }
            }

        val mediationUserReasonBool: Boolean
            get() {
                return (mediationUserReason?.length ?: 0) > 45
            }

        val mediationWorkerReasonBool: Boolean
            get() {
                return (mediationWorkerReason?.length ?: 0) > 45
            }

    }

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
        var inspectionRate: Double = 0.0,
        @SerialName("is_inspection")
        var isServiceInspection :String = ""

    )  {
        @Serializable
        data class Category(
            @SerialName("id")
            var id: String = "",
            @SerialName("title")
            var title: String = ""
        ) 
    }

    @Serializable
    data class Worker(
        @SerialName("contact")
        var contact: Contact = Contact(),
        @SerialName("description")
        var about: String? = "",
        @SerialName("id")
        var id: String? = null,
        @SerialName("first_name")
        var firstName: String = "",
        @SerialName("last_name")
        var lastName: String = "",
        @SerialName("portfolio")
        var portfolio: ModelProviders.Portfolio = ModelProviders.Portfolio(),
        @SerialName("profile")
        var profile: String = "",
        @SerialName("work_area")
        var workArea: ModelProviders.WorkArea = ModelProviders.WorkArea(),
        @SerialName("verification_status")
        var verificationStatus: String? = null,
        @SerialName("username")
        var username: String? = null,
        @SerialName("experience")
        var experience: Int? = null,

    )  {

        val fullName: String
            get() {
                return "$firstName $lastName"
            }

        @Serializable
        data class Contact(
            @SerialName("code")
            var code: String = "",
            @SerialName("number")
            var number: String = ""
        ) 
    }

    @Serializable
    data class Support(
        @SerialName("already_created")
        var alreadyCreated: Boolean = false,
        @SerialName("create_new")
        var createNew: Boolean = false,
        @SerialName("ticket")
        var ticket: Ticket? = Ticket(),
    ) {
        @Serializable
        data class Ticket(
            @SerialName("account")
            var account: Account? = Account(),
            @SerialName("conclusion")
            var conclusion: String = "",
            @SerialName("id")
            var id: String = "",
            @SerialName("status")
            var status: String = ""
        )

        @Serializable
        data class Account(
            @SerialName("first_name")
            var firstName: String = "",
            @SerialName("last_name")
            var lastName: String = "",
            @SerialName("profile")
            var profile: String = ""
        )
    }

    @Serializable
    data class Feedback(
        @SerialName("booking_id")
        val bookingId: Int? = 0,
        @SerialName("created_at")
        val createdAt: String? = "",
        @SerialName("custom_id")
        val customId: String? = "",
        @SerialName("deleted_at")
        val deletedAt: String? = "",
        @SerialName("id")
        val id: Int? = 0,
        @SerialName("is_finished")
        val isFinished: String? = "",
        @SerialName("is_started")
        val isStarted: String? = "",
        @SerialName("is_worked_for_hours")
        val isWorkedForHours: String? = "",
        @SerialName("is_worker_is_capable")
        val isWorkerIsCapable: String? = "",
        @SerialName("is_worker_show_up")
        val isWorkerShowUp: String? = "",
        @SerialName("updated_at")
        val updatedAt: String? = ""
    )
}


