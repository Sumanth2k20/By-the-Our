package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.request.BookingServiceRequest
import com.bytheour.data.request.CreateInspectionRequest
import com.bytheour.data.request.GetServiceProviderRequest
import com.bytheour.data.request.GetWorkerSpecificServicesRequest
import com.bytheour.data.request.GetWorkersRatingsRequest
import com.bytheour.data.request.InspectionReportRequest
import com.bytheour.data.request.PaymentDetailsRequest
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
import com.bytheour.data.response.PaymentDetailsResponse
import com.bytheour.data.response.PromoCodeListResponse
import com.bytheour.data.response.ServiceDetailsResponse
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

class UserBookingsRepository(
    private val httpClient: HttpClient
) {

    @OptIn(InternalAPI::class)
    suspend fun getPayment(payload: PaymentDetailsRequest): JtApiResult<PaymentDetailsResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CUSTOMER_GET_PAYMENT)
                formData {
                    body = FormDataContent(Parameters.build {
                        append("stripe_version", payload.stripeVersion ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun startBooking(payload: StartBookingRequest): JtApiResult<MakeChatArchiveResponse> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CUSTOMER_START_BOOKING)
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", payload.booking ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun completeRequestBooking(payload: StartBookingRequest): JtApiResult<MakeChatArchiveResponse> {

        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CUSTOMER_REQUEST_COMPLETE_BOOKING)
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", payload.booking ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun raiseMediation(request :RaiseMediationRequest): JtApiResult<CommonResponse> {
        return safeApiCallFormData(httpClient) {
            request{
                method = HttpMethod.Post
                url(JTURLS.raiseMediation())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", request.booking ?: "")
                        append("mediation_reason", request.mediationReason ?: "")
                        append("mediation_amount", request.mediationAmount ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun acceptMediation(
        request :RaiseMediationRequest
    ): JtApiResult<CommonResponse> {
        return safeApiCallFormData(httpClient) {
            request{
                method = HttpMethod.Post
                url(JTURLS.ACCEPT_MEDIATION())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", request.booking ?: "")
                        append("mediation_reason", request.mediationReason ?: "")
                        append("mediation_amount", request.mediationAmount ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun serviceBooking(request :BookingServiceRequest): JtApiResult<BookingServiceResponse> {
        return safeApiCall(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.manageAddBookings())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("service", request.service ?: "")
                        append("address", request.address ?: "")
                        append("hours", request.hours ?: "")
                        append("date", request.date ?: "")
                        append("time", request.time?:"")
                        append("timezone", request.timeZone ?: "")
                        append("latitude", request.latitude ?: "")
                        append("longitude", request.longitude ?: "")
                        append("inspection_booking_id",request.inspectionBookingId?:"")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun serviceDetails(request: ServiceDetailsRequest): JtApiResult<ServiceDetailsResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.serviceDetails())
                body = FormDataContent(Parameters.build {
                    append("service", request.serviceId?:"")
                    append("latitude", request.latitude?:"")
                    append("longitude", request.longitude?:"")
                })
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun getWorkerSpecificServices(request: GetWorkerSpecificServicesRequest): JtApiResult<GetWorkersSpecificServicesResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.getWorkerSpecificServices())
                body = FormDataContent(Parameters.build {
                    append("worker_id", request.workerId?:"")
                })
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun getProviderDetailsAndServices(request: GetServiceProviderRequest): JtApiResult<GetServiceProviderDetailsResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.getServiceProviderDetails())
                body = FormDataContent(Parameters.build {
                    append("service", request.service?:"")
                    append("limit", request.limit?:"")
                    append("offset", request.offset?:"")
                    append("provider_name", request.providerName?:"")
                })
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun getWorkerRatings(request:GetWorkersRatingsRequest): JtApiResult<GetWorkerRatingResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.getWorkersRatings())
                body = FormDataContent(Parameters.build {
//                    append("service", request.service?:"")
                    append("worker", request.worker?:"")
                    append("limit", request.limit)
                    append("offset", request.offset)
                    append("ratings", request.ratings?:"")
                })
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun getPromoCodesList(request: PromoCodeListRequest): JtApiResult<PromoCodeListResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.getPromoCodesList())
                body = FormDataContent(Parameters.build {
                    append("booking", request.bookingId?:"")
                })
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    //Inspection

    @OptIn(InternalAPI::class)
    suspend fun createInspection(request :CreateInspectionRequest): JtApiResult<CreateInspectionResponse> {
        return safeApiCall(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.inspectionCreate())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("service", request.service ?: "")
                        append("address", request.address ?: "")
                        append("date", request.date ?: "")
                        append("time", request.time?:"")
                        append("timezone", request.timeZone ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun submitInspectionReport(request: InspectionReportRequest): JtApiResult<AcceptBookingResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.inspectionReport())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", request.booking ?: "")
                        append("problem", request.problem ?: "")
                        append("problem_description", request.problemDescription ?: "")
                        append("problem_solution", request.problemSolution ?: "")
                        append("estimation", request.estimation?.toString() ?: "")
                        append("available_date_time", request.availableDateTime ?: "")

                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun acceptInspectionReport(request: StartBookingRequest): JtApiResult<AcceptInspectionReportResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.acceptInspectionReport())
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
    suspend fun declineInspectionReport(request: RejectInspectionRequest):JtApiResult<AcceptBookingResponse>{
        return safeApiCall(httpClient) {
            request{
                method = HttpMethod.Post
                url(JTURLS.rejectInspectionReport())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("booking", request.booking ?: "")
                        append("reason",request.reason?:"")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }




}
