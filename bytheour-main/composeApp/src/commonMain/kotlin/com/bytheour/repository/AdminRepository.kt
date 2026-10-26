package com.bytheour.repository

import com.bytheour.core.JTURLS
import com.bytheour.core.JTURLS.token
import com.bytheour.core.JtApiResult
import com.bytheour.core.safeApiCall
import com.bytheour.data.request.AdminDeleteUserRequest
import com.bytheour.data.request.AdminEditUserRequest
import com.bytheour.data.request.AdminGetUserRequest
import com.bytheour.data.request.ChangeUserStatusRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.request.UsersListRequest
import com.bytheour.data.response.ModelUserList
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.FormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.forms.submitForm
import io.ktor.client.request.headers
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.Parameters
import io.ktor.util.InternalAPI

class AdminRepository(
    private val httpClient: HttpClient
) {


    suspend fun getAdminDashBoardCount(): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_DASHBOARD_COUNT())
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }

        }
    }

    @OptIn(InternalAPI::class)
    suspend fun adminServiceRequestList(request: ServiceListRequest): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_SERVICE_REQUEST_LIST())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.limit?.let { append("limit", it) }
                        request.offset?.let { append("offset", it) }
                        request.keyWord?.let { append("keyword", it) }
                    })
                }
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun adminServiceManageList(request: ServiceListRequest): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_MANAGE_SERVICE_LIST())
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
    suspend fun getUsersList(request: UsersListRequest): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.USERS_LIST())
                formData {
                    body = FormDataContent(Parameters.build {
                        request.limit?.let { append("limit", it) }
                        request.offset?.let { append("offset", it) }
                        request.searchKey?.let { append("keyword", it) }
                    })
                }
                headers {
                    append("Authorization", "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun changeUserStatus(request: ChangeUserStatusRequest): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_CHANGE_USER_STATUS())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("id", request.id?:"")
                        append("status",request.status?:"")
                    })
                }
                headers {
                    append("Authorization", "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun adminDeleteUser(request: AdminDeleteUserRequest): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_DELETE_USER())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("id", request.id?:"")
                    })
                }
                headers {
                    append("Authorization", "Bearer ${token()}")
                }
            }
        }
    }

    @OptIn(InternalAPI::class)
    suspend fun adminGetUserData(request: AdminGetUserRequest): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_GET_USER_DATA())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("id", request.id?:"")
                    })
                }
                headers {
                    append("Authorization", "Bearer ${token()}")
                }
            }
        }
    }


    @OptIn(InternalAPI::class)
    suspend fun adminUserEdit(request: AdminEditUserRequest): JtApiResult<ModelUserList>{
        return safeApiCall(httpClient){
            request {
                method = HttpMethod.Post
                url(JTURLS.ADMIN_EDIT_USER())
                formData {
                    body = FormDataContent(Parameters.build {
                        append("id", request.id)
                        append("first_name", request.firstName)
                        append("last_name", request.lastName)
                        append("email", request.email)
                        append("profile", request.profile)
                    })
                }
                headers {
                    append("Authorization", "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun adminWorkerList(
        limit: String,
        offset: String,
        searchKey: String? = null,
        documentStatus: String? = null
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_WORKER_LIST(),
                formParameters = Parameters.build {
                    append("limit", limit)
                    append("offset", offset)
                    searchKey?.let { append("keyword", it) }
                    documentStatus?.let { append("document_status", it) }
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun changWorkerStatus(
        request: ChangeUserStatusRequest
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_CHANGE_WORKER_STATUS(),
                formParameters = Parameters.build {
                    append("id", request.id ?:"")
                    append("status", request.status ?:"")
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun deleteWorker(
        id: String,
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_DELETE_WORKER(),
                formParameters = Parameters.build {
                    append("id", id)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    suspend fun getAdminWorkerData(
        id: String,
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_GET_WORKER_DATA(),
                formParameters = Parameters.build {
                    append("id", id)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
    suspend fun getAdminDownloads(reportType: String): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_DOWNLOAD(),
                formParameters = Parameters.build {
                    append("report_type", reportType)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun adminEditServiceRequestList(
        id: String,
        title: String,
        description: String,
        categoryId: String,
        sequence: String,
        picture: String? = null,
        serviceStatus: String
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_EDIT_SERVICE_REQUEST_LIST(),
                formParameters = Parameters.build {
                    append("id", id)
                    append("title", title)
                    append("description", description)
                    append("category_id", categoryId)
                    append("sequence", sequence)
                    picture?.let { append("picture", it) }
                    append("service_status", serviceStatus)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun adminEditManageServiceRequest(
        id: String,
        title: String,
        description: String,
        categoryId: String,
        sequence: String,
        picture: String? = null,
        serviceStatus: String
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_EDIT_SERVICE_MANAGE(),
                formParameters = Parameters.build {
                    append("id", id)
                    append("title", title)
                    append("description", description)
                    append("category_id", categoryId)
                    append("sequence", sequence)
                    picture?.let { append("picture", it) }
                    append("service_status", serviceStatus)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }




    suspend fun getServicesList(
        limit: String,
        offset: String,
        keyword: String? = null
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_SERVICE_LIST(),
                formParameters = Parameters.build {
                    append("limit", limit)
                    append("offset", offset)
                    append("keyword", keyword ?: "")
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }


    suspend fun changServiceStatus(
        id: String,
        status: String
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_CHANGE_SERVICE_STATUS(),
                formParameters = Parameters.build {
                    append("id", id)
                    append("status", status)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun deleteService(
        id: String,
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_DELETE_SERVICE(),
                formParameters = Parameters.build {
                    append("id", id)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun adminEditWorker(
        id: String,
        firstname: String,
        lastName: String,
        profile: String,
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_EDIT_WORKER(),
                formParameters = Parameters.build {

                    append("id", id)
                    append("first_name", firstname)
                    append("last_name", lastName)
                    append("profile", profile)
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }

    suspend fun adminVerificationStatus(
        id: String,
        status: String? = null,
        reason: String? = null,
        rejectionStatus: String? = null
    ): JtApiResult<ModelUserList> {
        return safeApiCall(httpClient) {
            httpClient.submitForm(
                url = JTURLS.ADMIN_VERIFICATION_STATUS(),
                formParameters = Parameters.build {
                    append("id", id)
                    status?.let { append("status", it) }
                    reason?.let { append("reason", it) }
                    rejectionStatus?.let { append("rejection_status", it) }
                },
                encodeInQuery = false
            ) {
                headers {
                    append(HttpHeaders.Authorization, "Bearer ${token()}")
                }
            }
        }
    }
}
