package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.request.AddAddressRequest
import com.bytheour.data.request.DefaultAddressRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.AddAddressResponse
import com.bytheour.data.response.AddressListResponse
import com.bytheour.data.response.DefaultAddressResponse
import com.bytheour.data.response.GetTrendingServicesResponse
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

class UserRepository(
    private val httpClient: HttpClient
) {


    @OptIn(InternalAPI::class)
    suspend fun getAddressList(request: ServiceListRequest): JtApiResult<AddressListResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_ADDRESS())
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
    suspend fun defaultAddress(request: DefaultAddressRequest): JtApiResult<DefaultAddressResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CUSTOMER_DEFAULT_ADDRESS())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.id?.let { append("id", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    suspend fun getTrendingServices(): JtApiResult<GetTrendingServicesResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.TRENDING_SERVICES())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun deleteAddress(request: DefaultAddressRequest): JtApiResult<DefaultAddressResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CUSTOMER_DELETE_ADDRESS())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.id?.let { append("id", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun addAddressRepo(request: AddAddressRequest):JtApiResult<AddAddressResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CUSTOMER_ADD_ADDRESS())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("name", request.name ?: "")
                        append("house", request.house ?: "")
                        append("address_line_1", request.addressLine1 ?: "")
                        append("city", request.city ?: "")
                        append("state", request.state ?: "")
                        append("lat", request.latitude ?: "")
                        append("lng", request.longitude ?: "")
                        append("zip_code", request.zipCode ?: "")
                        append("id", request.id ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


}