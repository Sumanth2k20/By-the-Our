package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.AddRatingRequest
import com.bytheour.data.request.BookingDetailsRequest
import com.bytheour.data.request.CancelBookingRequest
import com.bytheour.data.request.ManageQuotationRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.BookingDetailResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.data.response.ManageQuotationResponse
import com.bytheour.data.response.ModelCustomerBookingList
import com.bytheour.repository.CommonBookingRepository
import com.bytheour.showProgress

class CommonBookingUseCase(
    private val commonBookingsRepository: CommonBookingRepository
) {

    suspend fun completedBookings(request: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {
        showProgress.value = true
        val result = commonBookingsRepository.completeBookings(request)
        showProgress.value = false
        return result
    }

    suspend fun cancelledBookings(request: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {
        showProgress.value = true
        val result = commonBookingsRepository.getCancelledBookings(request)
        showProgress.value = false
        return result
    }

    suspend fun getUpcomingList(payload: ServiceListRequest): JtApiResult<ModelCustomerBookingList?> {
        showProgress.value = true
        val result = commonBookingsRepository.getUpcoming(payload)
        showProgress.value = false
        return result
    }

    suspend fun getOngoingList(payload: ServiceListRequest): JtApiResult<ModelCustomerBookingList?> {
        showProgress.value = true
        val result = commonBookingsRepository.getOngoing(payload)
        showProgress.value = false
        return result
    }

    suspend fun getPendingList(payload: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {
        showProgress.value = true
        val result = commonBookingsRepository.getPending(payload)
        showProgress.value = false
        return result
    }

    suspend fun getBookingDetails(payload: BookingDetailsRequest): JtApiResult<BookingDetailResponse?> {
        showProgress.value = true
        val result = commonBookingsRepository.getBookingDetails(payload)
        showProgress.value = false
        return result
    }

    suspend fun cancelBooking(payload: CancelBookingRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = commonBookingsRepository.customerCancelBooking(payload)
        showProgress.value = false
        return result
    }

    suspend fun addRating(payload: AddRatingRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = commonBookingsRepository.addRating(payload)
        showProgress.value = false
        return result
    }
    suspend fun updateRating(payload: AddRatingRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = commonBookingsRepository.updtaeRating(payload)
        showProgress.value = false
        return result
    }
    suspend fun manageQuotation(request : ManageQuotationRequest):JtApiResult<ManageQuotationResponse>{
        showProgress.value = true
        val result = commonBookingsRepository.manageQuotation(request)
        showProgress.value = false
        return result
    }
}

