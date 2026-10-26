package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.data.request.CommonRateRequest
import com.bytheour.data.request.DeleteServiceRequest
import com.bytheour.data.request.ServiceByWorkerRequest
import com.bytheour.data.response.CommonRateResponse
import com.bytheour.data.response.GetCategoryResponse
import com.bytheour.data.response.MakeChatArchiveResponse
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

class ProviderServicesRepository(
    val httpClient: HttpClient
) {


    suspend fun getCategory():JtApiResult<GetCategoryResponse>{
        return safeApiCall(httpClient, block = {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_CATEGORY())
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        })
    }


    @OptIn(InternalAPI::class)
    suspend fun deleteService(payload: DeleteServiceRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCall(httpClient, block = {
            request {
               method = HttpMethod.Post
               url(JTURLS.DELETE_SERVICE())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("service",payload.service?:"")

                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        })
    }
    @OptIn(InternalAPI::class)
    suspend fun addService(options: Map<String, String>): JtApiResult<MakeChatArchiveResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADD_SERVICE())
                body = FormDataContent(
                    Parameters.build {
                        options.forEach { (key, value) ->
                            append(key, value)
                        }
                    }
                )
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    @OptIn(InternalAPI::class)
    suspend fun commonRate(
        payload: CommonRateRequest,
        useCommonRate:Boolean
    ):JtApiResult<CommonRateResponse>{
        return safeApiCall(httpClient, block = {
            request {
               method = HttpMethod.Post
               url(JTURLS.MANAGE_COM_RATE())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("common",payload.common?:"")
                        if(useCommonRate){
                            append("rate",payload.rate?:"")
                        }
                        append("min_hour",payload.min_hour?:"")

                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        })

    }

    @OptIn(InternalAPI::class)
    suspend fun serviceByWorker(payload: ServiceByWorkerRequest):JtApiResult<MakeChatArchiveResponse>{
        return safeApiCall(httpClient, block = {
            request {
               method = HttpMethod.Post
               url(JTURLS.MANAGE_SERVICES_BY_WORKER())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("category_id",payload.category_id?:"")
                        append("title",payload.title?:"")
                        append("description",payload.description?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        })
    }


}