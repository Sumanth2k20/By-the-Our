package com.bytheour.presentations.screens.tabs.explore.searchingAndRequestSentScreens


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.BookingServiceRequest
import com.bytheour.data.request.CreateInspectionRequest
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

data class AddressDetails(
    val id: String? = null,
    val locationType: String? = null,
    val flatNo: String? = null,
    val area: String? = null,
    val landmark: String? = null,
    val defaultAddress: Boolean? = false,
    val search: String? = "",
    val notServiceable: Boolean? = false,
    val latitude:String?= null,
    val longitude:String? = null,
)

class SelectionDateOfServiceViewModel (
    private val userBookingsUseCases: UserBookingsUseCases,
    private val userUseCase: UserUseCases,
):ViewModel(){
    data class SelectUIState(
        val selectedDate: LocalDate = LocalDate.now(),
        val selectedTab: Int= 0,
        val selectedTime: String? = null,
        val addressList: List<AddressDetails> = emptyList(),
        val message: String? = null,
        val isNavigate:Boolean? = null,
        val minRate : Int = 0,
        val maxRate : Int = 0,
        val serviceTitle:String?= null,
        val bookingId:String?= null,
        val showAlert:Boolean? = false,
        val isAddressFetchComplete: Boolean = false,
        val inspectionSuccess:Boolean? = null,
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
            when (val result = userUseCase.getAddressList(request)) {
                is JtApiResult.Success -> {
                    val addresses = result.data.data.map { addressData ->
                        AddressDetails(
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
                        it.copy(
                            addressList = addresses,
                            isAddressFetchComplete = true // ✅ mark complete
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isAddressFetchComplete = true // ✅ even if failed
                        )
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
        inspectionBookingId: String
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
                inspectionBookingId = inspectionBookingId
            )

            when (val result = userBookingsUseCases.serviceBooking(request)) {
                is JtApiResult.Success -> {
                    val bookingData = result.data.data
                    _uiState.update {
                        it.copy(
                            selectedDate = LocalDate.now(),
                            isNavigate = true,
                            bookingId = bookingData.booking.id
                        )
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
            when(val result = userBookingsUseCases.serviceDetails(request)){
                is JtApiResult.Success ->{
                    if (result.data.data != null){
                        _uiState.update {
                            it.copy(
                                maxRate = result.data.data.maxRate ?:0,
                                minRate = result.data.data.minRate ?:0,
                                serviceTitle = result.data.data.title,
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
        _uiState.update { it.copy(message = message) }
    }


    fun makeInspectionRequest(
        service: String?,
        address: String?,
        date: String?,
        time: String?,
        timeZone: String?,
    ) {
        viewModelScope.launch {
            val request = CreateInspectionRequest(
                service = service,
                address = address,
                date = date,
                time = time,
                timeZone = timeZone,
            )

            when (val result = userBookingsUseCases.createInspection(request)) {
                is JtApiResult.Success -> {
                    val bookingData = result.data.data
                    _uiState.update {
                        it.copy(
                            selectedDate = LocalDate.now(),
                            bookingId = bookingData?.booking?.id,
                            inspectionSuccess = true
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            inspectionSuccess = false
                        )
                    }
                }
            }
        }
    }


    fun clearNavigationFlags() {
        _uiState.update {
            it.copy(
                isNavigate = false,
                inspectionSuccess = false
            )
        }
    }




}