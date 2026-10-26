package com.bytheour.useCases

import com.bytheour.core.JtApiResult
import com.bytheour.data.request.AddAddressRequest
import com.bytheour.data.request.DefaultAddressRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.data.response.AddAddressResponse
import com.bytheour.data.response.AddressListResponse
import com.bytheour.data.response.DefaultAddressResponse
import com.bytheour.data.response.GetTrendingServicesResponse
import com.bytheour.repository.UserRepository
import com.bytheour.showProgress

class UserUseCases(
    private val userRepo: UserRepository
) {

    suspend fun getAddressList(request: ServiceListRequest): JtApiResult<AddressListResponse> {
        showProgress.value = true
        val result = userRepo.getAddressList(request)
        showProgress.value = false
        return result
    }

    suspend fun defaultAddress(request: DefaultAddressRequest): JtApiResult<DefaultAddressResponse> {
        showProgress.value = true
        val result = userRepo.defaultAddress(request)
        showProgress.value = false
        return result
    }

    suspend fun getTrendingServices(): JtApiResult<GetTrendingServicesResponse> {
        showProgress.value = true
        val result = userRepo.getTrendingServices()
        showProgress.value = false
        return result
    }

    suspend fun deleteAddress(request: DefaultAddressRequest): JtApiResult<DefaultAddressResponse> {
        showProgress.value = true
        val result = userRepo.deleteAddress(request)
        showProgress.value = false
        return result
    }

    suspend fun addAddressUseCase(request: AddAddressRequest): JtApiResult<AddAddressResponse?>{
        showProgress.value = true
        val result = userRepo.addAddressRepo(request)
        showProgress.value = false
        return result
    }
}
