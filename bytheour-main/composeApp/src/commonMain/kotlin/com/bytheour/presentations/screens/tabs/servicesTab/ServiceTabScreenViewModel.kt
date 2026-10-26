package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.CommonRateRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.useCases.ProviderServicesUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class Service(
    val name: String,
    val price: String,
    val minHours: String,
    val imageRes: String,
    val id: String? = null,
    val description: String? = null,
    val experience: String? = null,
    val isPermissions:String? = null,
    val catId:String? = null,
    val radius:String? = null,
    val iD:String? = null,
    val editDesc:String? = null


)

class ServiceTabScreenViewModel(
    private val providersUseCases: ProviderServicesUseCases,
    private val providerBookingUseCases: ProviderBookingUseCases,
    private val commonUseCases: CommonUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {

    data class UiState(
        val services: List<Service>          = emptyList(),
        val isCommonPricingEnabled: Boolean  = false,
        val userName: String                 = " ",
        val paginationOffset: Int            = 0,
        val reachedEnd: Boolean              = false,
        val isLoading: Boolean               = false,
        val errorMessage: String?            = null,
        val message: String?                 = null,
        val commonPrice: String?              = null,
        val commonHours:String?               =null,
        val profile:String? = null,
        val webUrl: String? = null,
        val isVerified:Boolean = false
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiStates = _uiState.asStateFlow()


    fun loadInitialServices() {
        _uiState.update { it.copy(services = emptyList(), paginationOffset = 0, reachedEnd = false) }
        loadServices(offset = 0, isInitialLoad = true)
    }

    fun loadMoreServices() {
        val state = _uiState.value
        if (state.isLoading || state.reachedEnd) return

        val nextOffset = state.paginationOffset + PAGINATION_LIMIT
        loadServices(offset = nextOffset, isInitialLoad = false)
    }


    fun toggleCommonPricing(enabled: Boolean) {
        _uiState.update { it.copy(isCommonPricingEnabled = enabled) }
    }

    private fun loadServices(offset: Int, isInitialLoad: Boolean) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val request = ServiceListRequest(
                limit  = PAGINATION_LIMIT.toString(),
                offset = offset.toString()
            )

            when (val result = providerBookingUseCases.getService(request)) {
                is Success -> {
                    val newServices = result.data.data.map { dto ->
                        Service(
                            name     = dto.service.title,
                            price    = dto.rate.toString(),
                            minHours = dto.minHour.toString(),
                            imageRes = dto.service.image,
                            id       = dto.id,
                            description = dto.service.description,
                            experience = dto.experience.toString(),
                            isPermissions = dto.serviceAtPremises,
                            catId = dto.category.id,
                            radius = dto.radius.toString(),
                            iD = dto.service.id,
                            editDesc = dto.description
                        )
                    }

                    _uiState.update { current ->
                        val combined = if (isInitialLoad) newServices
                        else current.services + newServices

                        current.copy(
                            services          = combined,
                            paginationOffset  = offset,
                            reachedEnd        = newServices.size < PAGINATION_LIMIT,
                            isLoading         = false,
                            message           = result.data.meta.message,
                            isCommonPricingEnabled = result.data.meta.common.allowed,
                            commonPrice = if (result.data.meta.common.allowed)
                                result.data.meta.common.rate.toString()
                            else current.commonPrice,
                            commonHours = if (result.data.meta.common.allowed)
                                result.data.meta.common.minHour.toString()
                            else current.commonHours
                        )
                    }
                }

                is Failure -> _uiState.update {
                    it.copy(
                        isLoading    = false,
                        errorMessage = result.error.message
                    )
                }
            }
        }
    }

    fun verifyAccountWithStripe(){
        viewModelScope.launch {
            val result = commonUseCases.verifyAccountWithStripe()
            when(result) {
                is Success -> {
                    val response = result.data.data
                    if (response.status=="verified") {
                        _uiState.update {
                            it.copy(
                                isVerified = response.status.equals("verified", ignoreCase = true),
                                message = "Your account has been Verified Successfully"
                            )
                        }
                    }else if (response.url != null){
                        _uiState.update {
                            it.copy(
                                webUrl = response.url
                            )
                        }
                    }else{
                        _uiState.update {
                            it.copy(
                                errorMessage = "Account couldn't be verified. Please try again"
                            )
                        }
                    }
                }

                is Failure -> {
                    _uiState.update {
                        it.copy(
                            errorMessage = "Account couldn't be verified. Please try again"
                        )
                    }
                }
            }
        }
    }

    fun updateWebUrl(){
        _uiState.update {
            it.copy(
                webUrl = null
            )
        }
    }

    fun getProfile(){
        viewModelScope.launch {
            val result = commonUseCases.getProfile()

            when(result){
                is Success -> {
                    if (result.data.data != null) {
                        sharedPreferenceUseCase.putUserInfo(result.data.data)
                        val name = "${result.data.data.firstName} " + result.data.data.lastName
                        _uiState.update {
                            it.copy(
                                userName = name,
                                profile = result.data.data.profile,
                                message = result.data.meta.message
                            )
                        }
                    }else{
                        _uiState.update {
                            it.copy(
                                message = result.data.meta.message
                            )
                        }
                    }
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }

            }
        }
    }


    fun submitCommonRate(
        commonHours: String?,
        commonPrice: String?,
        common:String?,
        onSuccess:()->Unit = {},
        gocommonRate:Boolean
    ) {

        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null, message = null) }

            val payload = CommonRateRequest(
                common   = common,
                rate     = commonPrice,
                min_hour = commonHours
            )

            when (val res = providersUseCases.commonRate(payload,gocommonRate)) {
                is Success -> {
                    _uiState.update { current ->
                        val updated = current.services.map { svc ->
                            svc.copy(
                                price    = state.commonPrice ?: svc.price,
                                minHours = state.commonHours ?: svc.minHours
                            )
                        }
                        current.copy(
                            services  = updated,
                            isLoading = false,
                            message   = res.data.meta.message,
                            commonPrice = commonPrice,
                            commonHours = commonHours
                        )
                    }
                    onSuccess()
                }
                is Failure -> _uiState.update {
                    it.copy(
                        isLoading    = false,
                        errorMessage = res.error.message
                    )
                }
            }
        }
    }
}
