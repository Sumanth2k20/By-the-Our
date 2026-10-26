package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.CommonRateRequest
import com.bytheour.data.request.DeleteServiceRequest
import com.bytheour.data.request.ServiceByWorkerRequest
import com.bytheour.data.response.CommonRateResponse
import com.bytheour.data.response.GetCategoryResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.repository.ProviderServicesRepository
import com.bytheour.showProgress

class ProviderServicesUseCases(
    private val providerServicesRepository: ProviderServicesRepository
) {

    suspend fun getCategory(): JtApiResult<GetCategoryResponse> {
        showProgress.value = true
        val result = providerServicesRepository.getCategory()
        showProgress.value = false
        return result
    }

    suspend fun deleteService(payload: DeleteServiceRequest): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerServicesRepository.deleteService(payload)
        showProgress.value = false
        return result
    }

    suspend fun addService(options: Map<String, String>): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerServicesRepository.addService(options)
        showProgress.value = false
        return result
    }

    suspend fun commonRate(payload: CommonRateRequest,gocommonRate:Boolean): JtApiResult<CommonRateResponse> {
        showProgress.value = true
        val result = providerServicesRepository.commonRate(payload,useCommonRate = gocommonRate)
        showProgress.value = false
        return result
    }

    suspend fun serviceByWorker(payload: ServiceByWorkerRequest): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerServicesRepository.serviceByWorker(payload)
        showProgress.value = false
        return result
    }
}
