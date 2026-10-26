package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.response.ProviderDashBoardResponse
import com.bytheour.repository.ProvidersRepository

class ProvidersUseCases(
    private val providerRepo: ProvidersRepository
) {
    suspend fun getProviderDashBoardDetails(): JtApiResult<ProviderDashBoardResponse?> {
       val result  = providerRepo.getProviderDashBoardDetails()
        return result
    }
}