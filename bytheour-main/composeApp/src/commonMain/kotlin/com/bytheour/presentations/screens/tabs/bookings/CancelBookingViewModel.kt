package com.bytheour.presentations.screens.tabs.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.CancelBookingRequest
import com.bytheour.data.response.CancelReasonsResponse
import com.bytheour.useCases.CommonBookingUseCase
import com.bytheour.useCases.GeneralUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CancelBookingViewModel(
    private val generalUseCases: GeneralUseCases,
    private val commonBookingsUseCases: CommonBookingUseCase,
):ViewModel() {

    data class CancelBookingUiState(
        val selectedReason: String = "",
        val message:String? = "",
        val reasons: List<CancelReasonsResponse.Data> = emptyList(),
        val otherReasonInput: String = "",
        val reasonId :String? = "",
        val bookingId :String? = "",
        val hourType :String? = "",
        val cancelAfterStartTime: String?= "",
        val isNavigate:Boolean? = null,
        val errorMessage: String? = null
    )



    private val _uiState = MutableStateFlow(CancelBookingUiState())
    val uiState: StateFlow<CancelBookingUiState> = _uiState.asStateFlow()

    fun selectReason(reasonData: CancelReasonsResponse.Data) {
        _uiState.update {
            it.copy(
                selectedReason = reasonData.reason,
                reasonId = reasonData.id
            )
        }
    }


    fun onOtherReasonInputChanged(value: String) {
        _uiState.update {
            it.copy(otherReasonInput = value)
        }
    }


    fun getCancelReasons() {
        viewModelScope.launch {
            when (val result = generalUseCases.getCancelReason()) {
                is Success -> {
                    val reasonList = result.data?.data.orEmpty()
                    val defaultSelected = reasonList.firstOrNull()

                    _uiState.update {
                        it.copy(
                            reasons = reasonList,
                            reasonId = defaultSelected?.id,
                            selectedReason = defaultSelected?.reason ?: "",
                        )
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



    fun cancelRequest(
        reasonId:String,
        bookingId:String,
        hourType :String?,
        text :String,
        cancelAfterStartTime:String
    ){
        val request = CancelBookingRequest(
            booking = bookingId,
            reasonId = reasonId,
            hourType = hourType,
            text = text,
            cancelAfterStartTime = cancelAfterStartTime
        )
        viewModelScope.launch {
            when (val result = commonBookingsUseCases.cancelBooking(payload = request)) {
                is Success -> {
                    _uiState.update {
                        it.copy(
                            message = result.data?.meta?.message,
                            isNavigate = true
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = result.error.message,
                            isNavigate = false
                        )
                    }
                }
            }
        }
    }

    fun clearSuccess(){
        _uiState.update {
            it.copy(message = null)
        }
    }

    fun clearError(){
        _uiState.update {
            it.copy(errorMessage = null)
        }
    }


}