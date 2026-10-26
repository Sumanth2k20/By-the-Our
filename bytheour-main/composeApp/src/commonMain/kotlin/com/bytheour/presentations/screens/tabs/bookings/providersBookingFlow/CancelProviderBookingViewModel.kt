package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

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

class CancelProviderBookingViewModel(
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
        val otherReasonError: String? = null
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
            it.copy(
                otherReasonInput = value
            )
        }
    }

    fun validateOtherReason(): Boolean {
        return if (uiState.value.selectedReason.equals(
                "Other Reason", ignoreCase = true
            )
            && uiState.value.otherReasonInput.isBlank()
        ) {
            _uiState.update { it.copy(otherReasonError = "Please Enter a Reason") }
            false
        } else {
            _uiState.update { it.copy(otherReasonError = null) }
            true
        }
    }


    fun getCancelReasons() {
        viewModelScope.launch {
            val result = generalUseCases.getCancelReason()

            when (result) {
                is Success -> {
                    val reasonList = result.data?.data.orEmpty()
                    val defaultSelected = reasonList.firstOrNull()

                    _uiState.update {
                        it.copy(
                            reasons = reasonList,
                            reasonId = defaultSelected?.id,
                            selectedReason = defaultSelected?.reason ?: ""
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
        cancelAfterStartTime:String,
        onSucess:()->Unit
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
                        )
                    }
                    onSucess()
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }



}