package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.request.AddSupportTicketRequest
import com.bytheour.data.request.CloseSupportTicketRequest
import com.bytheour.data.request.GetSupportHistoryRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.AddSupportTicketResponse
import com.bytheour.data.response.GetChatMessageHistoryResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.data.response.ModelProviderBookingList
import com.bytheour.data.response.TicketSupportResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI

class CommonChatSupportTicketRepository(private val httpClient: HttpClient) {


    @OptIn(InternalAPI::class)
    suspend fun getSupportTicket(payload: ServiceListRequest): JtApiResult<TicketSupportResponse> {
        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.SUPPORT_TICKET())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("limit",payload.limit ?:"")
                        append("offset", payload.offset?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getSupportHistory(payload: GetSupportHistoryRequest): JtApiResult<GetChatMessageHistoryResponse> {

        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_SUPPORT_HISTORY())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("support_id",payload.supportId ?:"")
                        append("limit",payload.limit ?:"")
                        append("offset", payload.offset?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun addSupportTicket(payload: AddSupportTicketRequest): JtApiResult<AddSupportTicketResponse> {
        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.ADD_SUPPORT_TICKET())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking",payload.booking ?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun getSupportBooking(payload: ServiceListRequest): JtApiResult<ModelProviderBookingList> {
        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_SUPPORT_BOOKING())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("limit",payload.limit ?:"")
                        append("offset", payload.offset?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun closedBooking(payload: CloseSupportTicketRequest): JtApiResult<MakeChatArchiveResponse> {

        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.CLOSE_SUPPORT_TICKET())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("support",payload.supportId ?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
}