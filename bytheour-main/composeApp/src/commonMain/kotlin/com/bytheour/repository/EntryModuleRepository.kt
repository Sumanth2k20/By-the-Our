package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.core.safeApiCallFormData
import com.bytheour.data.model.response.ModelUser
import com.bytheour.data.request.LoginRequest
import com.bytheour.data.request.UserEmailRequest
import com.bytheour.data.request.UserNameRequest
import com.bytheour.data.response.CheckEmailResponse
import com.bytheour.data.response.GetCheckMobile
import com.bytheour.data.response.LoginNewResponse
import com.bytheour.data.response.ModelTaxClassification
import com.bytheour.data.response.ModelUserList
import com.bytheour.data.response.UserNameResponse
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

class EntryModuleRepository(
    private val httpClient: HttpClient,
){


    @OptIn(InternalAPI::class)
    suspend fun checkUserName(request: UserNameRequest): JtApiResult<UserNameResponse>{
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                val url =
                    JTURLS.CHECK_USERNAME()
                url(url)
                formData {
                    body = FormDataContent(Parameters.build {
                        request.username?.let { append("field", it) }
                    })
                }
            }
        }
    }



    @OptIn(InternalAPI::class)
    suspend fun checkUserMail(request: UserEmailRequest): JtApiResult<CheckEmailResponse>{
        return safeApiCallFormData(httpClient) {
            request {
                method = HttpMethod.Post
                val url =
                    JTURLS.CHECK_EMAIL()
                url(url)
                formData {
                    body = FormDataContent(Parameters.build {
                        request.userMail?.let { append("field", it) }
                    })
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun checkUser(request: LoginRequest): JtApiResult<GetCheckMobile> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                val url =
                    JTURLS.LOGIN()
                url(url)
                formData {
                    body = FormDataContent(Parameters.build {
                        request.field?.let { append("field", it) }
                    })
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun login(
        contact: String?,
        securityToken: String?
    ): JtApiResult<LoginNewResponse> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post

                url(JTURLS.loginUserLogin())
                headers {
                    append(HttpHeaders.Authorization, "Bearer")
                }
                formData {
                    body = FormDataContent(Parameters.build {
                        contact?.let { append("contact", it) }
                        securityToken?.let { append("security_token", it) }
                    })
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun register(
        firstName: String,
        lastName: String,
        userName: String,
        email: String?,
        country: String,
        number: String,
        code: String?,
        profile: String?
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.register())
                headers {
                    append(HttpHeaders.Authorization, "Bearer")
                }
                body = FormDataContent(Parameters.build {
                    append("first_name", firstName)
                    append("last_name", lastName)
                    append("username", userName)
                    email?.let { append("email", it) }
                    append("contact[country]", country)
                    append("contact[number]", number)
                    code?.let { append("referral_code", it) }
                    profile?.let { append("profile", it) }
                })
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun saveProfile(
        firstName: String?,
        lastName: String?,
        email: String?,
        userName: String?,
        companyType: String?
    ): JtApiResult<ModelUser> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.saveProfile())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
                body = FormDataContent(Parameters.build {
                    firstName?.let { append("first_name", it) }
                    lastName?.let { append("last_name", it) }
                    email?.let { append("email", it) }
                    userName?.let { append("username", it) }
                    companyType?.let { append("company_type", it) }
                })
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun taxClassification(type:String?): JtApiResult<ModelTaxClassification> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post

                url(JTURLS.taxClassificationUrl())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
                formData {
                    body = FormDataContent(Parameters.build {
                        type?.let { append("type", it) }
                    })
                }
            }
        }
    }





}