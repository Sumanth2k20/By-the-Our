package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.BookingServiceRequest
import com.bytheour.data.request.CreateInspectionRequest
import com.bytheour.data.request.GetServiceProviderRequest
import com.bytheour.data.request.GetWorkerSpecificServicesRequest
import com.bytheour.data.request.GetWorkersRatingsRequest
import com.bytheour.data.request.InspectionReportRequest
import com.bytheour.data.request.PromoCodeListRequest
import com.bytheour.data.request.RaiseMediationRequest
import com.bytheour.data.request.RejectInspectionRequest
import com.bytheour.data.request.ServiceDetailsRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.data.response.AcceptBookingResponse
import com.bytheour.data.response.AcceptInspectionReportResponse
import com.bytheour.data.response.BookingServiceResponse
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.CreateInspectionResponse
import com.bytheour.data.response.GetServiceProviderDetailsResponse
import com.bytheour.data.response.GetWorkerRatingResponse
import com.bytheour.data.response.GetWorkersSpecificServicesResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.data.response.PromoCodeListResponse
import com.bytheour.data.response.ServiceDetailsResponse
import com.bytheour.repository.UserBookingsRepository
import com.bytheour.showProgress

class UserBookingsUseCases(
    private val userBookingsRepository: UserBookingsRepository
) {


    suspend fun startBooking(payload: StartBookingRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = userBookingsRepository.startBooking(payload)
        showProgress.value = false
        return result
    }

    suspend fun completeRequestBooking(payload: StartBookingRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = userBookingsRepository.completeRequestBooking(payload)
        showProgress.value = false
        return result
    }

    suspend fun raiseMediation(request: RaiseMediationRequest):JtApiResult<CommonResponse>{
        showProgress.value = true
        val result = userBookingsRepository.raiseMediation(request)
        showProgress.value = false
        return result
    }

    suspend fun acceptMediation(request: RaiseMediationRequest):JtApiResult<CommonResponse>{
        showProgress.value = true
        val result = userBookingsRepository.acceptMediation(request)
        showProgress.value = false
        return result
    }

    suspend fun serviceBooking(request : BookingServiceRequest): JtApiResult<BookingServiceResponse> {
        showProgress.value = true
        val result = userBookingsRepository.serviceBooking(request)
        showProgress.value = false
        return result
    }

    suspend fun serviceDetails(request: ServiceDetailsRequest): JtApiResult<ServiceDetailsResponse> {
        showProgress.value = true
        val result = userBookingsRepository.serviceDetails(request)
        showProgress.value = false
        return result
    }
    suspend fun getWorkerSpecificServices(request: GetWorkerSpecificServicesRequest): JtApiResult<GetWorkersSpecificServicesResponse> {
        showProgress.value = true
        val result = userBookingsRepository.getWorkerSpecificServices(request)
        showProgress.value = false
        return result
    }

    suspend fun getProviderDetailsAndServices(request: GetServiceProviderRequest): JtApiResult<GetServiceProviderDetailsResponse> {
        showProgress.value = true
        val result = userBookingsRepository.getProviderDetailsAndServices(request)
        showProgress.value = false
        return result
    }


    suspend fun getWorkerRatings(request: GetWorkersRatingsRequest): JtApiResult<GetWorkerRatingResponse> {
        showProgress.value = true
        val result = userBookingsRepository.getWorkerRatings(request)
        showProgress.value = false
        return result
    }

    suspend fun getPromoCodesList(request: PromoCodeListRequest): JtApiResult<PromoCodeListResponse>{
        showProgress.value = true
        val result = userBookingsRepository.getPromoCodesList(request)
        showProgress.value = false
        return result
    }


    //Inspection
    suspend fun createInspection(request : CreateInspectionRequest): JtApiResult<CreateInspectionResponse> {
        showProgress.value = true
        val result = userBookingsRepository.createInspection(request)
        showProgress.value = false
        return result
    }


    suspend fun submitInspectionReport(request: InspectionReportRequest): JtApiResult<AcceptBookingResponse> {
        showProgress.value = true
        val result = userBookingsRepository.submitInspectionReport(request)
        showProgress.value = false
        return result
    }

    suspend fun acceptInspectionReport(request: StartBookingRequest): JtApiResult<AcceptInspectionReportResponse> {
        showProgress.value = true
        val result = userBookingsRepository.acceptInspectionReport(request)
        showProgress.value = false
        return result
    }

    suspend fun declineInspectionReport(request: RejectInspectionRequest):JtApiResult<AcceptBookingResponse>{
        showProgress.value = true
        val result = userBookingsRepository.declineInspectionReport(request)
        showProgress.value = false
        return result
    }

}
