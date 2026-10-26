package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.model.response.ModelService
import com.bytheour.data.request.AcceptAndDeclineRequest
import com.bytheour.data.request.EstimationRequest
import com.bytheour.data.request.FixedEstimationRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.data.response.AcceptBookingResponse
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.GetWorkerBookingRequest
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.repository.ProviderBookingRepository
import com.bytheour.showProgress

class ProviderBookingUseCases(
    private val providerBookingsRepository: ProviderBookingRepository
) {

    suspend fun getService(request: ServiceListRequest): JtApiResult<ModelService> {
        showProgress.value = true
        val result = providerBookingsRepository.getServices(request)
        showProgress.value = false
        return result
    }

    suspend fun acceptBooking(request: AcceptAndDeclineRequest): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.acceptBooking(request)
        showProgress.value = false
        return result
    }

    suspend fun declineBooking(request: AcceptAndDeclineRequest): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.declineBooking(request)
        showProgress.value = false
        return result
    }

    suspend fun startJob(request: StartBookingRequest): JtApiResult<CommonResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.startJob(request)
        showProgress.value = false
        return result
    }
    suspend fun endJob(request: StartBookingRequest): JtApiResult<CommonResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.endJob(request)
        showProgress.value = false
        return result
    }

    suspend fun fixedEstimationBooking(request: FixedEstimationRequest): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.fixedEstimationBooking(request)
        showProgress.value = false
        return result
    }

    suspend fun sendEstimation(request: EstimationRequest): JtApiResult<MakeChatArchiveResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.sendEstimationBooking(request)
        showProgress.value = false
        return result
    }


    suspend fun getBookingRequest(): JtApiResult<GetWorkerBookingRequest> {
        val result = providerBookingsRepository.getBookingRequest()
        return result
    }

    suspend fun updateBookingRequest(request: StartBookingRequest): JtApiResult<AcceptBookingResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.updateBookingRequest(request)
        showProgress.value = false
        return result
    }

    suspend fun rejectBookingRequest(request: StartBookingRequest): JtApiResult<AcceptBookingResponse> {
        showProgress.value = true
        val result = providerBookingsRepository.rejectBookingRequest(request)
        showProgress.value = false
        return result
    }


}
