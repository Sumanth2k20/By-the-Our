package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.*
import com.bytheour.data.response.*
import com.bytheour.repository.CommonTransactionRepository
import com.bytheour.showProgress

class CommonTransactionUseCase(
    val transactionRepo: CommonTransactionRepository
) {

    suspend fun transactionList(payload: ServiceListRequest): JtApiResult<TransactionListResponse> {
        showProgress.value = true
        val result =  transactionRepo.transactionList(payload)
        showProgress.value = false
        return result
    }
    suspend fun transactionStripe(): JtApiResult<ModelCommon> {
        showProgress.value = true
        val result =  transactionRepo.stripTransaction()
        showProgress.value = false
        return result
    }

}