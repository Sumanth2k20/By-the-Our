package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.model.response.ModelService
import com.bytheour.data.request.AcceptAndDeclineRequest
import com.bytheour.data.request.EstimationRequest
import com.bytheour.data.request.FixedEstimationRequest
import com.bytheour.data.request.PaymentDetailsRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.request.StartBookingRequest
import com.bytheour.data.response.AcceptBookingResponse
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.GetWorkerBookingRequest
import com.bytheour.data.response.MakeChatArchiveResponse
import com.bytheour.data.response.PaymentDetailsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI

class ProviderBookingRepository(
    private val httpClient: HttpClient
) {


    @OptIn(InternalAPI::class)
    suspend fun getServices(payload:ServiceListRequest):JtApiResult<ModelService>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_GET_SERVICE())
                formData {
                    body =FormDataContent(Parameters.build {
                        payload.offset?.let { append("offset", it) }
                        payload.limit?.let { append("limit", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun acceptBooking(payload: AcceptAndDeclineRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_ACCEPT_BOOKINGS)
                formData {
                    body =FormDataContent(Parameters.build {
                        payload.booking?.let { append("booking", it) }
                        payload.timezone?.let { append("timezone", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun declineBooking(payload:AcceptAndDeclineRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_DECLINE_BOOKINGS)
                formData {
                    body =FormDataContent(Parameters.build {
                        payload.booking?.let { append("booking", it) }
                        payload.timezone?.let { append("timezone", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun startJob(payload: StartBookingRequest):JtApiResult<CommonResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.START_JOB())
                formData {
                    body =FormDataContent(Parameters.build {
                        payload.booking?.let { append("booking", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun endJob(payload: StartBookingRequest):JtApiResult<CommonResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.END_JOB())
                formData {
                    body =FormDataContent(Parameters.build {
                        payload.booking?.let { append("booking", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun fixedEstimationBooking(payload: FixedEstimationRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                    append(HttpHeaders.ContentType, "application/json")
                }
                url(JTURLS.PROVIDER_SEND_ESTIMATION())
                setBody(payload)

            }
        }
    }
    suspend fun sendEstimationBooking(payload: EstimationRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                    append(HttpHeaders.ContentType, "application/json")
                }
                url(JTURLS.PROVIDER_SEND_ESTIMATION())
                setBody(payload)

            }
        }
    }




    suspend fun getBookingRequest(): JtApiResult<GetWorkerBookingRequest> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.workerGetBookingRequest())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun updateBookingRequest(request: StartBookingRequest): JtApiResult<AcceptBookingResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.updateBookingRequest())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking",request.booking?:"")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun rejectBookingRequest(request: StartBookingRequest): JtApiResult<AcceptBookingResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.rejectBookingRequest())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking",request.booking?:"")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }




}
