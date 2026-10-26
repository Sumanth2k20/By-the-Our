package com.bytheour.presentations.screens.tabs.moreTab.transactionHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.BookingDetailsRequest
import com.bytheour.repository.CommonBookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate


data class BookingSummaryUiState(
    val serviceTitle: String = "",
    val bookingId: String = "",
    val serviceProviderName: String = "",
    val date: String = "",
    val time: String = "",
    val bookedHours: Double = 0.0,
    val extraHours: Double = 0.0,
    val totalHours: Double = 0.0,
    val paymentMethod: String = "",
    val amount: Double = 0.0,
    val convenienceFee: Double = 0.0,
    val totalAmount: Double = 0.0,
    val errorMessage: String? = null,
    val feePercent: Double = 0.0,
)

class TransactionDetailScreenViewModel(
    private val repository: CommonBookingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BookingSummaryUiState())
    val uiState: StateFlow<BookingSummaryUiState> = _uiState.asStateFlow()

    fun fetchBookingDetails(bookingId: String) {
        viewModelScope.launch {

            when (val response = repository.getBookingDetails(BookingDetailsRequest(bookingId))) {
                is JtApiResult.Success -> {
                    val data = response.data.data

                    _uiState.value = BookingSummaryUiState(
                        serviceTitle = data.service.title,
                        bookingId = data.booking.id,
                        serviceProviderName = "${data.worker.firstName} ${data.worker.lastName}",
                        date = formatDate(data.booking.date),
                        time = data.booking.time,
                        bookedHours = data.booking.hours,
                        extraHours = data.additional.hours?.hours ?: 0.0,
                        totalHours = (data.booking.hours) + (data.additional.hours?.hours ?: 0.0),
                        paymentMethod = data.bill.payment.mode?:"",
                        amount = data.bill.amount,
                        convenienceFee = data.bill.commission.percentageAmount,
                        totalAmount = data.bill.total,
                        feePercent = data.bill.commission.percentage
                    )
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy( errorMessage = response.error.message)
                    }
                }
            }
        }
    }

    private fun formatDate(dateString: String): String {
        return try {
            val date: LocalDate = LocalDate.parse(dateString)
            val day = date.dayOfMonth
            val suffix = when (day) {
                1, 21, 31 -> "st"
                2, 22 -> "nd"
                3, 23 -> "rd"
                else -> "th"
            }

            val monthName = when (date.monthNumber) {
                1 -> "January"
                2 -> "February"
                3 -> "March"
                4 -> "April"
                5 -> "May"
                6 -> "June"
                7 -> "July"
                8 -> "August"
                9 -> "September"
                10 -> "October"
                11 -> "November"
                12 -> "December"
                else -> ""
            }

            "$day$suffix $monthName ${date.year}"
        } catch (e: Exception) {
            dateString
        }
    }
}
