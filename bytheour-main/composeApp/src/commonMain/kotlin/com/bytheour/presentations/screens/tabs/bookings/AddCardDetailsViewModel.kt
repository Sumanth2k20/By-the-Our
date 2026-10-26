package com.bytheour.presentations.screens.tabs.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddCardDetailsViewModel(
    private val commonPaymentUseCase: CommonPaymentUseCase
) : ViewModel() {

    data class UIStates(
        val cardNumber: String? = null,
        val expiry: String? = null,
        val cvc: String? = null,
        val errorMessageCardNumber: String? = null,
        val errorMessageExpiry: String? = null,
        val errorMessageCvc: String? = null,
        val cardDetails: List<CardUIState> = emptyList(),
        val message: String? = null,
        val bookingId: String = "",
        val isErrorMsg: Boolean = false,
        val clientSecret: String? = null
    )

    data class CardUIState(
        val cardType: String? = null,
        val cardExpiry: String? = null,
        val lastFourDigits: String? = null,
    )

    private val _uiStates = MutableStateFlow(UIStates())
    val uiStates = _uiStates.asStateFlow()

    fun updateBookingId(bookingId: String) {
        _uiStates.update {
            it.copy(bookingId = bookingId)
        }
    }

    fun updateCard(cardNumber: String) {
        // Remove all non-digit characters first
        val digitsOnly = cardNumber.replace("[^0-9]".toRegex(), "")

        if (digitsOnly.length <= 16 && digitsOnly.all { it.isDigit() }) {
            // Format with spaces every 4 digits
            val formatted = digitsOnly.chunked(4).joinToString(" ")

            _uiStates.update {
                it.copy(cardNumber = formatted, errorMessageCardNumber = null, isErrorMsg = false)
            }
        }
    }

    fun updateExpiry(expiry: String) {
        val digitsOnly = expiry.filter { it.isDigit() }

        // Format based on input length
        val formatted = when {
            digitsOnly.isEmpty() -> ""
            digitsOnly.length <= 2 -> digitsOnly
            else -> "${digitsOnly.take(2)}/${digitsOnly.drop(2).take(2)}"
        }

        // Limit to 5 characters (MM/YY)
        val expiryNumber = if (formatted.length > 5) formatted.take(5) else formatted

        _uiStates.update {
            it.copy(expiry = expiryNumber, errorMessageExpiry = null)
        }
    }

    fun updateCvc(cvc: String?) {
        val cleanedCvc = cvc?.filter { it.isDigit() }?.take(3)
        _uiStates.update {
            it.copy(cvc = cleanedCvc, errorMessageCvc = null)
        }
    }

    fun validation(): Boolean {
        val currentNumber = uiStates.value
        val spaceCurrentNumber = currentNumber.cardNumber?.replace(" ", "") ?: ""
        val mobileNumberError = when {
            spaceCurrentNumber.isBlank() -> "Please Enter Card Number"
            spaceCurrentNumber.length != 16 -> "Card Number Must Be Exactly 16 Digits"
            !spaceCurrentNumber.all { it.isDigit() } -> "Card Number Must Contain Only Digits"
            else -> ""
        }
        val expiryError = when {
            currentNumber.expiry?.isBlank() == true -> "Please Enter Expiry Date"
            currentNumber.expiry?.length != 5 -> "Invalid Expiry Date Format"
            else -> null
        }

        val cvcError = when {
            currentNumber.cvc?.isBlank() == true -> "Please Enter CVC"
            currentNumber.cvc?.length !in 3..4 -> "CVC Must Be 3 Digits"
            else -> null
        }

        _uiStates.update { currentState ->
            currentState.copy(
                errorMessageCardNumber = mobileNumberError,
                errorMessageExpiry = expiryError,
                errorMessageCvc = cvcError
            )
        }

        return mobileNumberError.isEmpty() && expiryError?.isEmpty() == true && cvcError?.isEmpty() == true
    }

    fun getCard() {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.getCards()) {
                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(message = result.error.message)
                    }
                }

                is JtApiResult.Success -> {

                    val cardDetails= result.data.data.map {
                        CardUIState(
                            cardType = it.type,
                            cardExpiry = "${it.expMonth}/${it.expYear}",
                            lastFourDigits = it.last4
                        )
                    }
                    _uiStates.update {
                        it.copy(cardDetails = cardDetails)
                    }


                }

            }
        }
    }
}
