package com.bytheour.presentations.screens.tabs.moreTab.bankDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonPaymentUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BankAccountInfo(
    val accountHolderName: String = "",
    val last4Digits: String = "",
    val routingNumber: String = "",
    val bankName: String = "",
    val stripeAccountId: String = ""
)

data class BankDetailsUiState(
    val hasBankDetails: Boolean = false,
    val link: String = "",
    val successUrl: String = "",
    val chargesEnabled: Boolean = false,
    val payoutsEnabled: Boolean = false,
    val accountStatus: String = "",
    val bankAccounts: List<BankAccountInfo> = emptyList(),
    val message :String? = ""
)

class BankDetailsViewModel(
    private val commonPaymentUseCase: CommonPaymentUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(BankDetailsUiState())
    val uiState: StateFlow<BankDetailsUiState> = _uiState.asStateFlow()

    fun getBankDetailsOrStripeConnect() {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.getBankDetailsStripeConnect()) {
                is JtApiResult.Success -> {
                    val data = result.data.data

                    val bankAccountsList = data.bankAccount?.dataBankAccount?.map { bankAccount ->
                        BankAccountInfo(
                            accountHolderName = bankAccount.accountHolderName ?: "",
                            last4Digits = bankAccount.last4,
                            routingNumber = bankAccount.routeNumber,
                            bankName = bankAccount.bankName ?: "",
                            stripeAccountId = bankAccount.account
                        )
                    } ?: emptyList()
                    _uiState.update {
                        it.copy(
                            hasBankDetails = data.hasBankAccount,
                            link = data.link ?: "",
                            successUrl = data.successUrl ?: "",
                            accountStatus = data.status?:"",
                            bankAccounts = bankAccountsList,
                            message =result.data.meta.message,
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            hasBankDetails = false,
                            link = "",
                            successUrl = "",
                            chargesEnabled = false,
                            payoutsEnabled = false,
                            bankAccounts = emptyList(),
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }


    fun deleteBankDetailsOrStripeConnect() {
        viewModelScope.launch {
            when (val result = commonPaymentUseCase.deleteBankDetailsStripeConnect()) {
                is JtApiResult.Success -> {
                    _uiState.update {
                        it.copy(
                            message = result.data.meta.message
                        )
                    }
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