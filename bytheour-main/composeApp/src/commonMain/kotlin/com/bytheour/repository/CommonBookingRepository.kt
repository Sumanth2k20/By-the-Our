package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.request.AddRatingRequest
import com.bytheour.data.request.BookingDetailsRequest
import com.bytheour.data.request.CancelBookingRequest
import com.bytheour.data.request.ManageQuotationRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.BookingDetailResponse
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.data.response.ManageQuotationResponse
import com.bytheour.data.response.ModelCustomerBookingList
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

class CommonBookingRepository(
    private val httpClient: HttpClient
) {

    @OptIn(InternalAPI::class)
    suspend fun completeBookings(request: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.COMPLETE_BOOKINGS())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.limit?.let { append("limit", it) }
                        request.offset?.let { append("offset", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getCancelledBookings(request: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CANCELLED_BOOKINGS())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.limit?.let { append("limit", it) }
                        request.offset?.let { append("offset", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getUpcoming(payload: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {

        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.UPCOMING_LIST())
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
    suspend fun getOngoing(payload: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {
        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.ONGOING_LIST())
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
    suspend fun getPending(payload: ServiceListRequest): JtApiResult<ModelCustomerBookingList> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PENDING_LIST())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("limit", payload.limit ?: "")
                        append("offset", payload.offset ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getBookingDetails(payload: BookingDetailsRequest): JtApiResult<BookingDetailResponse> {

        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.BOOKING_DETAILS())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("id",payload.id ?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun customerCancelBooking(payload: CancelBookingRequest): JtApiResult<MakeChatArchiveResponse> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CANCEL_REQUEST_BOOKING())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", payload.booking ?: "")
                        append("reason_id", payload.reasonId ?: "")
                        append("text", payload.text ?: "")
                        append("hour_type", payload.hourType ?: "")
                        append("cancel_after_start_time", payload.cancelAfterStartTime ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun addRating(payload: AddRatingRequest
    ): JtApiResult<MakeChatArchiveResponse> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_ADD_RATINGS())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", payload.booking ?: "")
                        append("service_rating", payload.serviceRating ?: "")
                        append("rating", payload.rating ?: "")
                        append("review", payload.review ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun updtaeRating(payload: AddRatingRequest
    ): JtApiResult<MakeChatArchiveResponse> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_UPDATE_RATINGS())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("id", payload.booking ?: "")
                        append("service_rating", payload.serviceRating ?: "")
                        append("rating", payload.rating ?: "")
                        append("review", payload.review ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun manageQuotation(request : ManageQuotationRequest):JtApiResult<ManageQuotationResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.manageQuotation())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking_id", request.bookingId ?: "")
                        append("booking_time", request.bookingTime ?: "")
                        append("status", request.status ?: "")
                        append("user_time", request.userTime ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
}