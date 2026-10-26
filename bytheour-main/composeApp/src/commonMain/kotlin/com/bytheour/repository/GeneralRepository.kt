package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.GOOGLE_PLACE_DETAILS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.request.FaqRequest
import com.bytheour.data.request.GetServicesRequest
import com.bytheour.data.request.LoginRequest
import com.bytheour.data.request.SearchServiceRequest
import com.bytheour.data.response.AppVersionResponse
import com.bytheour.data.response.CancelReasonsResponse
import com.bytheour.data.response.GetAppStatusResponse
import com.bytheour.data.response.GetCheckMobile
import com.bytheour.data.response.GetTrendingServicesResponse
import com.bytheour.data.response.MediationGuidelineResponse
import com.bytheour.data.response.ModelCommon
import com.bytheour.data.response.ModelExpertVideo
import com.bytheour.data.response.ModelFaqResponse
import com.bytheour.data.response.PlaceDetailsResponse
import com.bytheour.data.response.PlacesAutocompleteResponse
import com.bytheour.data.response.Prediction
import com.bytheour.data.response.PrivacyAndPolicyResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.parameter
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI

class GeneralRepository(
    private val httpClient: HttpClient,
) {


    suspend fun getMediationGuideline(): JtApiResult<MediationGuidelineResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.MEDIATION_GUIDELINES)
            }
        }
    }

    suspend fun getPlacePredictions(query: String, apiKey: String): List<Prediction> {
        val result = safeApiCall<PlacesAutocompleteResponse>(httpClient) {
            get(JTURLS.GOOGLE_PLACES()) {
                parameter("input", query)
                parameter("key", apiKey)
                parameter("types", "address") // Optional: filter by address type
            }
        }

        return when (result) {
            is JtApiResult.Success -> result.data.predictions ?: emptyList()
            is JtApiResult.Failure -> {
                println("Error: ${result.error.message}")
                emptyList()
            }
        }
    }

    suspend fun getPlaceDetails(placeId: String, apiKey: String): PlaceDetailsResponse? {
        val result = safeApiCall<PlaceDetailsResponse>(httpClient) {
            get(GOOGLE_PLACE_DETAILS()) {
                parameter("place_id", placeId)
                parameter("key", apiKey)
                parameter("fields", "geometry,formatted_address")
            }
        }

        return when (result) {
            is JtApiResult.Success -> result.data
            is JtApiResult.Failure -> {
                println("Error: ${result.error.message}")
                null
            }
        }
    }

    suspend fun appStatus():JtApiResult<GetAppStatusResponse>{
        return safeApiCall(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.APP_STATUS)
            }
        }
    }


    suspend fun getPrivacyAndPolicy():JtApiResult<PrivacyAndPolicyResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PRIVACY_POLICY)
            }
        }
    }



    suspend fun getAppVersion(): JtApiResult<AppVersionResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_APP_VERSION)
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getFaq(request: FaqRequest): JtApiResult<ModelFaqResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.FAQ)
                formData {
                    body = FormDataContent(Parameters.build {
                        append("limit", request.limit ?: "")
                        append("offset", request.offset ?: "")
                        append("type", request.type ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun login(request: LoginRequest): JtApiResult<GetCheckMobile?> {

        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.loginUserLogin(),
                formParameters = Parameters.build {
                    request.field?.let { append("field", it) }
                }
            )
        }
    }



    suspend fun cancelReasons(): JtApiResult<CancelReasonsResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.CANCEL_REASONS)
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun getServices(request: GetServicesRequest): JtApiResult<GetTrendingServicesResponse> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_SERVICE())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("category_id", request.categoryId ?: "")
                        append("limit", request.limit ?: "")
                        append("offset", request.offset ?: "")
                        append("keyword", request.keyword ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun searchService(request : SearchServiceRequest): JtApiResult<GetTrendingServicesResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.searchServices())
                formData{
                    body = FormDataContent(Parameters.build {
                        append("limit", request.limit ?: "")
                        append("offset", request.offset ?: "")
                        append("keyword", request.searchKey ?: "")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun deviceToken(
        device: String,
        token: String,
        type: String,
        version: Int,
        os: String,
        appVersion: String
    ): JtApiResult<ModelCommon> {
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.DEVICE_TOKEN())
                body = FormDataContent(Parameters.build {
                    append("device", device)
                    append("token", token)
                    append("type", type)
                    append("version", version.toString())
                    append("os", os)
                    append("app_version", appVersion)
                })
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun getExportsVideos():JtApiResult<ModelExpertVideo>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.getExpertsVideos())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


}