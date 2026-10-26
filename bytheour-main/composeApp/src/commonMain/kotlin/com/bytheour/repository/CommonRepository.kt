package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.model.response.ModelDocList
import com.bytheour.data.model.response.ModelGetRating
import com.bytheour.data.request.SwitchAccountRequest
import com.bytheour.data.response.CommonResponse
import com.bytheour.data.response.LoginNewResponse
import com.bytheour.data.response.GetNotificationStatusResponse
import com.bytheour.data.response.ModelDocUrl
import com.bytheour.data.response.ModelInviteCode
import com.bytheour.data.response.ModelNotificationList
import com.bytheour.data.response.ModelSwitchStatus
import com.bytheour.data.response.StripeManageAccountModel
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.request.put
import io.ktor.client.request.request
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI

class CommonRepository(
    private val httpClient: HttpClient,
) {



    suspend fun updateNotificationStatus(allowPush: String): JtApiResult<LoginNewResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.NOTIFICATION_STATUS(),
                formParameters = Parameters.build {
                    append("allow_push_notification", allowPush)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun updateVisibility(avilability: String): JtApiResult<GetNotificationStatusResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.updateVisibility(),
                formParameters = Parameters.build {
                    append("visible", avilability)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun getProfile(): JtApiResult<LoginNewResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.GET_PROFILE())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun updateProfile(
        firstName: String?,
        lastName: String?,
        email: String?,
        country: String?,
        number: String?,
        profile: String?,
        about: String?,
        userName :String?
    ): JtApiResult<LoginNewResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.UPDATE_PROFILE(),
                formParameters = Parameters.build {
                    firstName?.let { append("first_name", it) }
                    lastName?.let { append("last_name", it) }
                    email?.let { append("email", it) }
                    country?.let { append("country", it) }
                    number?.let { append("number", it) }
                    profile?.let { append("profile", it) }
                    about?.let { append("about", it) }
                    userName?.let { append("user_name", it) }
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }





    suspend fun sendReportAnIssue(
        subject: String,
        description: String,
    ): JtApiResult<CommonResponse> {
    return safeApiCall(httpClient) {
        httpClient.submitForm(
            url = JTURLS.REPORT_ISSUE(),
            formParameters = Parameters.build {
                append("subject", subject)
                append("message", description)
            },
            encodeInQuery = false
        ){
            headers {
                append(HttpHeaders.Authorization, "Bearer ${token()}")
            }
        }
    }
    }


    suspend fun getProfile(
        contentType: String,
        extension: String,
    ): JtApiResult<ModelDocUrl> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url =JTURLS.GET_PROFILE_URL_USER(),
                formParameters = Parameters.build {
                    append("contentType", contentType)
                    append("extension", extension)
                },
                encodeInQuery = false
            ){
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun getGalleryImageUrl(
        contentType: String,
        extension: String,
    ): JtApiResult<ModelDocUrl> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GALLERY_GENERATE_URL(),
                formParameters = Parameters.build {
                    append("contentType", contentType)
                    append("extension", extension)
                },
                encodeInQuery = false
            ){
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun uploadAwsDocSafe(
        uploadUrl: String,
        fileBytes: ByteArray,
    ): JtApiResult<Unit> {
        return safeApiCall(httpClient) {
            put(uploadUrl) {
                setBody(fileBytes)
            }
        }
    }


    suspend fun deleteAccount(
        reason: String,
    ): JtApiResult<CommonResponse> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.DELETEACCOUNT(),
                formParameters = Parameters.build {
                    append("reason", reason)
                },
                encodeInQuery = false
            ){
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun logOut():JtApiResult<CommonResponse>{
        return safeApiCall(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.LOG_OUT())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun getRating(
        limit: String,
        offset: String,
        ratingType: String?,
        ratings: String?,
        serviceRating: String?
    ): JtApiResult<ModelGetRating> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.GET_RATING(),
                formParameters = Parameters.build {
                    append("limit", limit)
                    append("offset", offset)
                    ratingType?.let { append("rating_type", it) }
                    ratings?.let { append("ratings", it) }
                    serviceRating?.let { append("service_rating", it) }
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun getNotificationList(
        limit: String,
        offset: String): JtApiResult<ModelNotificationList> {
        return safeApiCallFormData(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.getNotificationList())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("limit", limit)
                        append("offset", offset)
                    })
                }
                headers{
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun switchAccount(
        payload: SwitchAccountRequest
    ):JtApiResult<LoginNewResponse>{
        return safeApiCall(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.SWITCH_ACCOUNT())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("contact", payload.contact?:"")
                        append("security_token", payload.securityToken?:"")
                        append("type", payload.type?:"")
                        append("email", payload.email?:"")
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    suspend fun switchAccountStatus(): JtApiResult<ModelSwitchStatus> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.SWITCH_ACCOUNT_STATUS())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun getDocuments(): JtApiResult<ModelDocList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.PROVIDER_GET_DOCUMENT())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }



    suspend fun generateChatAttachmentUrl(
        contentType: String,
        extension: String,
    ): JtApiResult<ModelDocUrl> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url =JTURLS.generateChatUrl(),
                formParameters = Parameters.build {
                    append("contentType", contentType)
                    append("extension", extension)
                },
                encodeInQuery = false
            ){
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }

    }
    suspend fun verifyAccountWithStripe(): JtApiResult<StripeManageAccountModel> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.stripeVerificationUrl())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    suspend fun getInviteCode(): JtApiResult<ModelInviteCode> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.INVITE_CODE())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

}