package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.LoginRequest
import com.bytheour.data.request.UserEmailRequest
import com.bytheour.data.request.UserNameRequest
import com.bytheour.data.response.CheckEmailResponse
import com.bytheour.data.response.GetCheckMobile
import com.bytheour.data.response.LoginNewResponse
import com.bytheour.data.response.ModelTaxClassification
import com.bytheour.data.response.ModelUserList
import com.bytheour.data.response.UserNameResponse
import com.bytheour.repository.EntryModuleRepository
import com.bytheour.showProgress

class EntryModuleUseCases<ModelUser>(private val entryModuleRepository: EntryModuleRepository) {

    suspend fun checkUserNameUseCase(request: UserNameRequest): JtApiResult<UserNameResponse> {
        showProgress.value = true
        val result = entryModuleRepository.checkUserName(request)
        showProgress.value = false
        return result
    }

    suspend fun checkUserMailUseCase(request: UserEmailRequest): JtApiResult<CheckEmailResponse> {
        showProgress.value = true
        val result = entryModuleRepository.checkUserMail(request)
        showProgress.value = false
        return result
    }

    suspend fun loginUseCase(request: LoginRequest): JtApiResult<GetCheckMobile> {
        showProgress.value = true
        val result = entryModuleRepository.checkUser(request)
        showProgress.value = false
        return result
    }

    suspend fun login(
        contact: String?,
        securityToken: String?
    ): JtApiResult<LoginNewResponse> {
        return entryModuleRepository.login(
            contact?:"",
            securityToken?:""
        )
    }

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
        return entryModuleRepository.register(
            firstName,
            lastName,
            userName,
            email,
            country,
            number,
            code,
            profile
        )
    }

    suspend fun saveProfile(
        firstName: String?,
        lastName: String?,
        email: String?,
        userName: String?,
        companyType: String?
    ): JtApiResult<com.bytheour.data.model.response.ModelUser> {
        return entryModuleRepository.saveProfile(
            firstName = firstName,
            lastName = lastName,
            email = email,
            userName = userName,
            companyType = companyType
        )
    }

    suspend fun tacClassification(type:String?): JtApiResult<ModelTaxClassification> {
        return entryModuleRepository.taxClassification(type)
    }


}
