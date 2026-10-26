package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.ModelCommon
import com.bytheour.data.response.TransactionListResponse
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

class CommonTransactionRepository(
    val httpClient: HttpClient
) {



    @OptIn(InternalAPI::class)
    suspend fun transactionList(payload: ServiceListRequest): JtApiResult<TransactionListResponse> {
        return safeApiCall(httpClient, block = {
            request {
                method = HttpMethod.Post
                url(JTURLS.TRANSACTION_LIST())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("limit",payload.limit?:"")
                        append("offset",payload.offset?:"")
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        })
    }

    suspend fun stripTransaction(): JtApiResult<ModelCommon> {
        return safeApiCall(httpClient, block = {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_STRIPE_CONNECT_WITHDRAW())
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        })
    }
}