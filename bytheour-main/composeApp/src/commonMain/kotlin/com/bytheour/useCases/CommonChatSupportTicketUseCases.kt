package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.AddSupportTicketRequest
import com.bytheour.data.request.CloseSupportTicketRequest
import com.bytheour.data.request.GetSupportHistoryRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.AddSupportTicketResponse
import com.bytheour.data.response.GetChatMessageHistoryResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.data.response.TicketSupportResponse
import com.bytheour.repository.CommonChatSupportTicketRepository
import com.bytheour.showProgress

class CommonChatSupportTicketUseCases(
    private val userRepository: CommonChatSupportTicketRepository
) {

    suspend fun getSupportTicket(payload: ServiceListRequest): JtApiResult<TicketSupportResponse?> {
        showProgress.value = true
        val result = userRepository.getSupportTicket(payload)
        showProgress.value = false
        return result
    }

    suspend fun getSupportHistory(payload: GetSupportHistoryRequest): JtApiResult<GetChatMessageHistoryResponse?> {
        showProgress.value = true
        val result = userRepository.getSupportHistory(payload)
        showProgress.value = false
        return result
    }

    suspend fun addTicketSupport(payload: AddSupportTicketRequest): JtApiResult<AddSupportTicketResponse?> {
        showProgress.value = true
        val result = userRepository.addSupportTicket(payload)
        showProgress.value = false
        return result
    }

    suspend fun getSupportBooking(payload: ServiceListRequest): JtApiResult<ModelProviderBookingList?> {
        showProgress.value = true
        val result = userRepository.getSupportBooking(payload)
        showProgress.value = false
        return result
    }

    suspend fun closeTicket(payload: CloseSupportTicketRequest): JtApiResult<MakeChatArchiveResponse?> {
        showProgress.value = true
        val result = userRepository.closedBooking(payload)
        showProgress.value = false
        return result
    }
}
