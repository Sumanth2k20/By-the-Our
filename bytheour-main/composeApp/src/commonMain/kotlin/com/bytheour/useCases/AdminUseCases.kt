package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.*
import com.bytheour.data.response.ModelUserList
import com.bytheour.repository.AdminRepository
import com.bytheour.showProgress

class AdminUseCases(
    private val adminRepository: AdminRepository
) {

    suspend fun getAdminDashBoardCount(): JtApiResult<ModelUserList?> {
        showProgress.value = true
        val result = adminRepository.getAdminDashBoardCount()
        showProgress.value = false
        return result
    }

    suspend fun getServicesList(limit: String, offset: String,keyword: String? = null): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.getServicesList(limit, offset, keyword = keyword)
        showProgress.value = false
        return result
    }

    suspend fun changeServiceStatus(id: String, status: String): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.changServiceStatus(id, status)
        showProgress.value = false
        return result
    }

    suspend fun adminDeleteService(id: String): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.deleteService(id)
        showProgress.value = false
        return result
    }

    suspend fun getAdminWorkersList(
        offset: String,
        limit: String,
        documentStatus: String? = null,
        searchKey: String? = null
    ): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminWorkerList(offset, limit, searchKey, documentStatus)
        showProgress.value = false
        return result
    }

    suspend fun changeWorkerStatus(payload:ChangeUserStatusRequest): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.changWorkerStatus(payload)
        showProgress.value = false
        return result
    }

    suspend fun adminDeleteWorker(id: String): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.deleteWorker(id)
        showProgress.value = false
        return result
    }

    suspend fun getUsersList(request: UsersListRequest): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.getUsersList(request)
        showProgress.value = false
        return result
    }

    suspend fun changeUserStatus(request: ChangeUserStatusRequest): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.changeUserStatus(request)
        showProgress.value = false
        return result
    }

    suspend fun adminDeleteUser(request: AdminDeleteUserRequest): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminDeleteUser(request)
        showProgress.value = false
        return result
    }

    suspend fun adminGetUserData(request: AdminGetUserRequest): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminGetUserData(request)
        showProgress.value = false
        return result
    }

    suspend fun adminUserEdit(request: AdminEditUserRequest): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminUserEdit(request)
        showProgress.value = false
        return result
    }

    suspend fun getAdminDownloadCount(): JtApiResult<ModelUserList?> {
        showProgress.value = true
        val result = adminRepository.getAdminDownloads(reportType = "total")
        showProgress.value = false
        return result
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
        showProgress.value = true
        val result = adminRepository.adminEditServiceRequestList(id, title, description, categoryId, sequence, picture, serviceStatus)
        showProgress.value = false
        return result
    }

    suspend fun adminEditManageServiceRequestList(
        id: String,
        title: String,
        description: String,
        categoryId: String,
        sequence: String,
        picture: String? = null,
        serviceStatus: String
    ): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminEditManageServiceRequest(id, title, description, categoryId, sequence, picture, serviceStatus)
        showProgress.value = false
        return result
    }

    suspend fun getAdminWorkerData(id: String): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.getAdminWorkerData(id)
        showProgress.value = false
        return result
    }

    suspend fun adminEditWorker(
        id: String,
        firstName: String,
        lastName: String,
        profile: String,
    ): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminEditWorker(id, firstName, lastName, profile)
        showProgress.value = false
        return result
    }

    suspend fun adminVerificationStatus(
        id: String,
        status: String? = null,
        reason: String? = null,
        rejectionStatus: String? = null
    ): JtApiResult<ModelUserList> {
        showProgress.value = true
        val result = adminRepository.adminVerificationStatus(id, status, reason, rejectionStatus)
        showProgress.value = false
        return result
    }

    suspend fun getAdminServiceRequest(request: ServiceListRequest): JtApiResult<ModelUserList?> {
        showProgress.value = true
        val result = adminRepository.adminServiceRequestList(request)
        showProgress.value = false
        return result
    }

    suspend fun getAdminServiceManage(request: ServiceListRequest): JtApiResult<ModelUserList?> {
        showProgress.value = true
        val result = adminRepository.adminServiceManageList(request)
        showProgress.value = false
        return result
    }
}
