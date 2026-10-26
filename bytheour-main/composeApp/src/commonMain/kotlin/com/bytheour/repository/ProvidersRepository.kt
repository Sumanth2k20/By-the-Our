package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.data.response.ProviderDashBoardResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod

class ProvidersRepository(
    private val httpClient: HttpClient
) {

    suspend fun getProviderDashBoardDetails(): JtApiResult<ProviderDashBoardResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_DASHBOARD_DETAILS())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

}