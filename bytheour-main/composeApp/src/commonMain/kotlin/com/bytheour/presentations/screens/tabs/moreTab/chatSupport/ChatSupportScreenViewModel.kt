package com.bytheour.presentations.screens.tabs.moreTab.chatSupport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.AddSupportTicketRequest
import com.bytheour.data.request.BookingDetailsRequest
import com.bytheour.data.request.CloseSupportTicketRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.data.response.SupportData
import com.bytheour.useCases.CommonBookingUseCase
import com.bytheour.useCases.CommonChatSupportTicketUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChatSupportScreenViewModel (
    private val chatSupportScreenUseCase: CommonChatSupportTicketUseCases,
    private val commonBookingUseCase: CommonBookingUseCase
) : ViewModel() {

    data class UiState(
        val ticket: List<SupportData>? = null,
        val supportBookings: List<ModelProviderBookingList.Data>? = null,
        val isLoading: Boolean = false,
        val error: String? = null,
        val serviceName: String = "",
        val bookingId: String = "",
        val providerName: String = "",
        val providerPhone: String? = null,
        val serviceRating: Double = 0.0,
        val serviceStatus :String? = "",
        val serviceImage :String? = "",
        val time :String?= "",
        val date :String?= "",
        val address:String?= "",
        val paymentMethod :String? = "",
        val amount :String? = "",
        val convenienceFee :String? = "",
        val bookedHours :String?="",
        val extraHours :String?= ""

    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun getSupportTicket(payload: ServiceListRequest) {
        viewModelScope.launch {

            when (val result = chatSupportScreenUseCase.getSupportTicket(payload)) {
                    is JtApiResult.Success ->
                        _uiState.update {
                            it.copy(ticket = result.data?.data)
                        }
                    is JtApiResult.Failure ->{
                        _uiState.update {
                            it.copy(error = result.error.message)
                        }
                    }

                }
            }

    }

    fun getSupportBookings(payload: ServiceListRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            when (val result = chatSupportScreenUseCase.getSupportBooking(payload)) {
                is JtApiResult.Success -> _uiState.update {
                    it.copy(supportBookings = result.data?.data, isLoading = false)
                }
                is JtApiResult.Failure -> _uiState.update {
                    it.copy(error = result.error.message, isLoading = false)
                }
            }
        }
    }

    fun getBookingDetails(id:String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = commonBookingUseCase.getBookingDetails(BookingDetailsRequest(id))) {
                is JtApiResult.Success -> {
                    val data = result.data?.data
                    _uiState.update {
                        it.copy(
                           serviceName = data?.service?.title?:"",
                            bookingId = data?.booking?.id?:"",
                            providerName = data?.worker?.firstName ?: ("" + data?.worker?.lastName),
                            providerPhone = data?.worker?.contact?.number?:"",
                            serviceRating = data?.review?.rating?.toDouble()?:0.0,
                            isLoading = false,
                            serviceStatus = data?.flags?.type,
                            serviceImage = data?.service?.image,
                            time = data?.booking?.time,
                            date = data?.booking?.date,
                            address = data?.address?.address?.fullAdd,
                            paymentMethod = data?.bill?.payment?.mode,
                            amount = data?.bill?.amount?.toString(),
                            convenienceFee = data?.bill?.commission?.percentageAmount.toString(),
                            bookedHours = data?.booking?.hours.toString(),
                            extraHours = data?.additional?.hours?.hours.toString()
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            error = result.error.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun addSupportTicket(payload: AddSupportTicketRequest, onResult: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = chatSupportScreenUseCase.addTicketSupport(payload)) {
                is JtApiResult.Success -> {
                    onResult()
                    _uiState.update {
                        it.copy(isLoading = false)
                    }
                }
                is JtApiResult.Failure -> {
                    val errorMessage = result.error.message
                    onResult()
                    _uiState.update { it.copy(error = errorMessage, isLoading = false) }
                }
            }
        }
    }
    fun closeSupportTicket(
        payload: CloseSupportTicketRequest,
        onResult: () -> Unit
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = chatSupportScreenUseCase.closeTicket(payload)) {
                is JtApiResult.Success -> {
                    onResult()
                    _uiState.update { it.copy(isLoading = false) }
                }
                is JtApiResult.Failure -> {
                    val errorMessage = result.error.message
                    onResult()
                    _uiState.update { it.copy(error = errorMessage, isLoading = false) }
                }
            }
        }
    }


}
