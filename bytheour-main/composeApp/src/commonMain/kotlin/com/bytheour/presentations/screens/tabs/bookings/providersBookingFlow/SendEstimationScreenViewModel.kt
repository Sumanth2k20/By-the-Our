package com.bytheour.presentations.screens.tabs.bookings.providersBookingFlow

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.EstimationRequest
import com.bytheour.data.request.FixedEstimationRequest
import com.bytheour.useCases.ProviderBookingUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EstimateViewModel (
    private val providerBookingUseCases: ProviderBookingUseCases
): ViewModel() {

    data class UiState(
        val priceType: String = "",
        val hourlyHours: String = "",
        val fixedHours: String = "",
        val fixedPrice: String = "",
        val message:String = "",
        val isSuccess:Boolean = false,
        val totalHourlyCost: String = "0",
    )

    private val _uiStates = MutableStateFlow(UiState())
    val uiStates: StateFlow<UiState> = _uiStates

    fun selectPriceType(type: String) {
        _uiStates.update {
            it.copy(priceType = type)
        }
    }

    fun clearMessage() {
        _uiStates.update { it.copy(message = "") }
    }


    fun updateHourlyHours(hours: String) {
        _uiStates.update { it.copy(hourlyHours = hours) }
    }

    fun setBaseCost(cost: String) {
        _uiStates.update { current ->
            val hourlyValue = _uiStates.value.hourlyHours.toIntOrNull() ?: 0
            val baseCost = cost.toIntOrNull() ?: 0
            current.copy(
                totalHourlyCost = (hourlyValue * baseCost).toString()
            )
        }
    }


    fun updateFixedHours(hours: String) {
        _uiStates.update { it.copy(fixedHours = hours) }
    }

    fun updateFixedPrice(price: String) {
        _uiStates.update { it.copy(fixedPrice = price) }
    }

    fun sendFixedEstimation(
        bookingId: String,
        estimatedHours: String?,
        priceType: String?,
        startTimes: List<String>,
        endTimes: List<String>,
        fixedPrice: String? = null,
        onSuccess:()->Unit
    ) {
        viewModelScope.launch {
            val request = FixedEstimationRequest(
                bookingId = bookingId,
                estimatedHours = estimatedHours,
                priceType = priceType,
                startTime = startTimes,
                endTime = endTimes,
                fixedPrice = fixedPrice
            )

            when (val result = providerBookingUseCases.fixedEstimationBooking(request = request)) {
                is JtApiResult.Success -> {
                    val message = result.data.meta.message.orEmpty()
                    val isSuccessMessage = message.contains("success", ignoreCase = true)

                    _uiStates.update {
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
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun sendEstimation(
        bookingId: String,
        estimatedHours: String?,
        priceType: String?,
        startTimes: List<String>,
        endTimes: List<String>,
        onSuccess:()->Unit
    ) {
        viewModelScope.launch {
            val request = EstimationRequest(
                bookingId = bookingId,
                estimatedHours = estimatedHours,
                priceType = priceType,
                startTime = startTimes,
                endTime = endTimes,
            )

            when (val result = providerBookingUseCases.sendEstimation(request = request)) {
                is JtApiResult.Success -> {
                        val message = result.data.meta.message.orEmpty()
                        val isSuccessMessage = message.contains("success", ignoreCase = true)

                        _uiStates.update {
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
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }
}
