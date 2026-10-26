package com.bytheour.presentations.screens.tabs.moreTab.transactionHistory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.CommonTransactionUseCase
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


data class TransactionItem(
    val id: String,
    val message: String,
    val type: String,
    val status: String,
    val amount: Double,
    val date: String,
    val booking: String?,
    val transactionsType: String?,

)

class TransactionHistoryScreenViewModel(
    private val transactionHistoryUseCase: CommonTransactionUseCase
) : ViewModel() {

    data class TransactionHistoryUiState(
        val transactions: List<TransactionItem> = emptyList(),
        val isLoading: Boolean = false,
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val paginationOffset: Int = 0,
        val errorMessage: String? = null,
        val walletAmount:String? = null,
        val holdAmount:String? = null,
        val stripeMessage:String?= ""
    )



    private val _uiState = MutableStateFlow(TransactionHistoryUiState())
    val uiState: StateFlow<TransactionHistoryUiState> = _uiState.asStateFlow()

    fun loadMoreTransactions() {
        val state = _uiState.value
        if (!state.isLoadingMore && state.hasMoreData) {
            loadTransactionList(offset = state.paginationOffset, isInitialLoad = false)
        }
    }


    fun fetchTransactionList() {
        _uiState.update {
            it.copy(
                transactions = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        loadTransactionList(offset = 0, isInitialLoad = true)
    }


    private fun loadTransactionList(offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            if (isInitialLoad) {
                _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            } else {
                _uiState.update { it.copy(isLoadingMore = true) }
            }

            val payload = ServiceListRequest(
                limit = PAGINATION_LIMIT.toString(),
                offset = offset.toString()
            )

            when (val result = transactionHistoryUseCase.transactionList(payload)) {
                is JtApiResult.Success -> {
                    val transactionDots = result.data.data
                    val transactions = transactionDots.map { dto ->
                        TransactionItem(
                            id = dto.id,
                            message = dto.message,
                            type = dto.type,
                            status = dto.status,
                            amount = dto.amount,
                            date = dto.date,
                            booking = dto.booking,
                            transactionsType = dto.transactionsType
                        )
                    }

                    val hasMore = transactions.size >= PAGINATION_LIMIT

                    _uiState.update { currentState ->
                        val updatedList = if (isInitialLoad) {
                            transactions
                        } else {
                            currentState.transactions + transactions
                        }

                        currentState.copy(
                            transactions = updatedList,
                            isLoading = false,
                            isLoadingMore = false,
                            paginationOffset = offset + transactions.size,
                            hasMoreData = hasMore,
                            walletAmount = result.data.meta.amount?.wallet.toString(),
                            holdAmount = result.data.meta.amount?.hold.toString()
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isLoadingMore = false,
                            errorMessage = result.error.message
                        )
                    }
                }
            }
        }
    }

     fun transactionsStripe(){
        viewModelScope.launch {
            when(val result =transactionHistoryUseCase.transactionStripe()){
                is JtApiResult.Success ->{
                    _uiState.update {
                        it.copy(stripeMessage =result.data.meta.message )
                    }
                }
                is JtApiResult.Failure ->{
                    _uiState.update {
                        it.copy(stripeMessage =result.error.message )
                    }
                }

            }
        }
    }
    fun clearError(){
        _uiState.update {
            it.copy(
                stripeMessage = null
            )
        }
    }


}
