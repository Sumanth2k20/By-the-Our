package com.bytheour.presentations.screens.tabs.bookings.inspection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.RejectInspectionRequest
import com.bytheour.presentations.screens.tabs.bookings.inspection.InspectionReportScreenViewModel.InspectionReportScreenUIState
import com.bytheour.useCases.UserBookingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InspectionDeclineViewModel(
    private val userBookingsUseCases: UserBookingsUseCases
): ViewModel() {

    data class InspectionDeclineReportUIState(
        val reason: String? = null,
        val reasonError: String? = null,
        val other: String? = null,
        val otherError: String? = null,
        val message :String? = null,
        val isNavigate:Boolean ? = null
    )

    private val _uiStates = MutableStateFlow(InspectionDeclineReportUIState())
    val uiStates = _uiStates.asStateFlow()


    fun onReasonChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(
            reason = value,
            reasonError = null,
            other = if (value != "Other") null else _uiStates.value.other,
            otherError = null
        )
    }

    fun onOtherChanged(value: String) {
        _uiStates.value = _uiStates.value.copy(other = value, otherError = null)
    }

    fun isValid():Boolean {
        val current = _uiStates.value
        var isValid = true

        var reasonError: String? = null
        var otherError: String? = null

        if (current.reason.isNullOrBlank()) {
            reasonError = "reason is required"
            isValid = false
        }

        if (current.reason == "Other" && current.other.isNullOrBlank()) {
            otherError = "other reason is required"
            isValid = false
        }


        _uiStates.value = current.copy(
            otherError = otherError,
            reasonError = reasonError,
        )
        return isValid
    }

    fun declineInspectionReport(
        bookingId: String,
        reason:String,
    ){
        viewModelScope.launch {
            val request = RejectInspectionRequest(
                booking = bookingId,
                reason = reason
            )
            when (val result = userBookingsUseCases.declineInspectionReport(request)) {
                is Success -> {
                    _uiStates.update {
                        it.copy(
                            message = result.data.meta?.message,
                            isNavigate = true
                        )
                    }
                }
                is JtApiResult.Failure ->{
                    _uiStates.update {
                        it.copy(message = result.error.message,
                            isNavigate = false
                        )
                    }
                }
            }
        }
    }
}