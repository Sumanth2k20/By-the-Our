package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.RaiseMediationRequest
import com.bytheour.useCases.UserBookingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class MediationRefundViewModelProvider(
    private val userBookingsUseCases: UserBookingsUseCases,
) : ViewModel() {

    data class UiState(
        val selectedOption: String = "",
        val reasonText: String = "",
        val reasonTextError:String? = null,
        val message: String = "",
        val isSuccess: Boolean = false,
        val isLoading: Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState

    fun onOptionSelected(option: String) {
        _uiState.update { it.copy(selectedOption = option) }
    }

    fun onReasonChanged(reason: String) {
        _uiState.update { it.copy(
            reasonText = reason,
            reasonTextError = null
        ) }
    }

    fun validate(): Boolean {
        var isValid = true

        if (_uiState.value.reasonText.trim().isEmpty()) {
            _uiState.update {
                it.copy(reasonTextError = "Reason Cannot Be Empty")
            }
            isValid = false
        }

        if (_uiState.value.selectedOption.isBlank()) {
            // If you want to show an error for selection too, you can add that here
            isValid = false
        }

        return isValid
    }
    fun onAcceptMediation(
        bookingId: String,
        mediationReason: String,
        mediationAmount: String,
        onSuccess:() -> Unit = {}
    ){
        viewModelScope.launch {
            val request = RaiseMediationRequest(
                booking = bookingId,
                mediationReason = mediationReason,
                mediationAmount = mediationAmount
            )
            val result = userBookingsUseCases.acceptMediation(request)

            when(result){
                is Success -> {
                    val message = result.data.meta.message.orEmpty()
                    val isSuccessMessage = message.contains("success", ignoreCase = true)

                    _uiState.update {
                        it.copy(
                            message = message,
                            isSuccess = isSuccessMessage
                        )
                    }

                    if (isSuccessMessage) {
                        onSuccess()
                    }

                }

                is JtApiResult.Failure -> {
                    val errorMessage = result.error.message
                    _uiState.update {
                        it.copy(message = errorMessage)
                    }
                }
            }
        }
    }
}
