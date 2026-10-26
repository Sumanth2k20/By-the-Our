package com.bytheour.data.response


import com.bytheour.data.Meta
import com.bytheour.data.model.response.Cost
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.model.response.Times
import com.bytheour.data.response.ModelProviderBookingList.InspectionReport
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ModelCustomerBookingList(
    @SerialName("data")
    var `data`: List<Data> = emptyList(),
    @SerialName("meta")
    var meta: Meta = Meta()
) {
    @Serializable
    data class Data(
        @SerialName("feedback")
        var feedback: ModelProviderBookingList.Feedback? = ModelProviderBookingList.Feedback(),
        @SerialName("address")
        var address: ModelProviderBookingList.Address? = ModelProviderBookingList.Address(),
        @SerialName("worker_address")
        var workerAddress: ModelProviderBookingList.WorkerAddress? = ModelProviderBookingList.WorkerAddress(),
        @SerialName("bill")
        var bill: ModelProviderBookingList.Bill = ModelProviderBookingList.Bill(),
        @SerialName("booking")
        var booking: ModelProviderBookingList.Booking = ModelProviderBookingList.Booking(),
        @SerialName("customer")
        var customer: ModelProviderBookingList.Customer = ModelProviderBookingList.Customer(),
        @SerialName("flags")
        var flags: ModelProviderBookingList.Flags = ModelProviderBookingList.Flags(),
        @SerialName("service")
        var service: ModelProviderBookingList.Service = ModelProviderBookingList.Service(),
        @SerialName("worker")
        var worker: ModelProviderBookingList.Worker = ModelProviderBookingList.Worker(),
        @SerialName("additional")
        var additional: ModelProviderBookingList.Additional = ModelProviderBookingList.Additional(),
        @SerialName("review")
        var review: ModelGetRating.Data? = ModelGetRating.Data(),
        @SerialName("cancellation")
        var cancellation: ModelProviderBookingList.Cancellation? = ModelProviderBookingList.Cancellation(),
        @SerialName("support")
        var support: ModelProviderBookingList.Support? = ModelProviderBookingList.Support(),
        @SerialName("mediation")
        var mediation: ModelProviderBookingList.Mediation = ModelProviderBookingList.Mediation(),
        @SerialName("cost")
        var cost: Cost = Cost(),
        @SerialName("times")
        var times: List<Times> = listOf(),
        @Transient
        var isFromNotification : Boolean = false,
        @Transient
        var distance : String = "",
        @Transient
        var stringBold : String = "",
        @Transient
        var customerTimeSlot :String? = "",
        @SerialName("inspection_report")
        val inspectionReport : InspectionReport? = InspectionReport()
    )
}
