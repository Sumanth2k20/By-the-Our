package com.bytheour.presentations.screens.tabs.bookings.inspection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.BookingServiceRequest
import com.bytheour.data.request.ServiceDetailsRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.UserBookingsUseCases
import com.bytheour.useCases.UserUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import network.chaintech.kmp_date_time_picker.utils.now

data class AddressDetailsI(
    val id: String? = null,
    val locationType: String? = null,
    val flatNo: String? = null,
    val area: String? = null,
    val landmark: String? = null,
    val defaultAddress: Boolean? = false,
    val search: String? = "",
    val notServiceable: Boolean? = false,
    val latitude:String?= null,
    val longitude:String? = null
)

class SelectingDateForBookingViewModel (
    private val userBookingsUseCases: UserBookingsUseCases,
    private val userUseCase: UserUseCases,
):ViewModel(){
    data class SelectUIState(
        val selectedDate: LocalDate = LocalDate.now(),
        val selectedTab: Int= 0,
        val selectedTime: String? = null,
        val addressList: List<AddressDetailsI> = emptyList(),
        val message: String? = null,
        val isNavigate:Boolean? = null,
        val minRate : Int = 0,
        val maxRate : Int = 0,
        val serviceTitle:String?= null,
        val bookingId:String?= null,
        val showAlert:Boolean? = false
    )

    private val _uiState = MutableStateFlow(SelectUIState())
    val uiState =_uiState.asStateFlow()


    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index, selectedTime = null) } // Reset time on tab change
    }

    fun onTimeSelected(time: String) {
        _uiState.update { it.copy(selectedTime = time) }
    }

    fun onDateSelected(date: LocalDate) {
        _uiState.update { it.copy(selectedDate = date) }
    }

    fun clearError() {
        _uiState.update { it.copy(message = null) }
    }

    fun getAddressList(
        limit: String,
        offset: Int,
    ) {
        viewModelScope.launch {
            val request = ServiceListRequest(
                limit = limit,
                offset = offset.toString()
            )
            val result = userUseCase.getAddressList(request)
            when (result) {
                is JtApiResult.Success -> {
                    val addresses = result.data.data.map { addressData ->
                        AddressDetailsI(
                            locationType = addressData.name,
                            flatNo = addressData.address.fullAdd,
                            area = addressData.address.city,
                            landmark = addressData.address.line2,
                            defaultAddress = addressData.isDefault,
                            notServiceable = !addressData.isEnable,
                            id = addressData.id,
                            longitude = addressData.lng,
                            latitude = addressData.lat
                        )
                    }
                    _uiState.update {
                        it.copy(addressList = addresses)
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }


    fun bookingService(
        service: String?,
        latitude: String,
        longitude: String,
        address: String?,
        hours: String?,
        date: String?,
        time: String?,
        timeZone: String?,
    ) {
        viewModelScope.launch {
            val request = BookingServiceRequest(
                service = service,
                latitude = latitude,
                longitude = longitude,
                address = address,
                hours = hours,
                date = date,
                time = time,
                timeZone = timeZone,
            )

            val result = userBookingsUseCases.serviceBooking(request)

            when (result) {
                is JtApiResult.Success -> {
                    val bookingData = result.data.data
                    if (bookingData != null) {
                        _uiState.update {
                            it.copy(
                                selectedDate = LocalDate.now(),
                                isNavigate = true,
                                bookingId = bookingData.booking.id
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                message = result.data.meta.message,
                                isNavigate = false
                            )
                        }
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isNavigate = false
                        )
                    }
                }
            }
        }
    }



    fun getServiceDetails(
        serviceId: String,
        lat:String,
        long:String
    ){
        viewModelScope.launch {
            val request = ServiceDetailsRequest(
                serviceId = serviceId,
                latitude = lat,
                longitude = long,
            )
            val result = userBookingsUseCases.serviceDetails(request)
            when(result){
                is JtApiResult.Success ->{
                    if (result.data.data != null){
                        _uiState.update {
                            it.copy(
                                maxRate = result.data.data?.maxRate?:0,
                                minRate = result.data.data?.minRate?:0,
                                serviceTitle = result.data.data?.title,
                            )
                        }
                    }else{
                        _uiState.update {
                            it.copy(
                                message = result.data.meta?.message,
                                isNavigate = false,
                                showAlert = true
                            )
                        }
                    }

                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
//                            message = result.error.message,
                            isNavigate = false,
                            showAlert = true
                        )
                    }
                }
            }
        }
    }


    fun showSnackBarMessage(message: String) {
        _uiState.update { currentState ->
            currentState.copy(message = message)
        }
    }




}