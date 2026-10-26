package com.bytheour.presentations.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTBtoMainScreenViewModel(
    sharedPreferenceUseCase: SharedPreferenceUseCase,
    private val providerBookingUseCases:ProviderBookingUseCases
): ViewModel() {


    private val _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()

    private val _hasWorkerServices = MutableStateFlow(true)
    val hasWorkerServices = _hasWorkerServices.asStateFlow()


    fun onNavItemClicked(index: Int) {
        _selectedIndex.update { index }
    }


    fun getWorkerServices() {
        viewModelScope.launch {
            val request = ServiceListRequest(
                limit  = PAGINATION_LIMIT.toString(),
                offset = 0.toString()
            )

            when (val result = providerBookingUseCases.getService(request)) {
                is Success -> {
                    val services = result.data.data
                    _hasWorkerServices.value = services.isNotEmpty()
                }
                is Failure -> {
                    _hasWorkerServices.value = false
                }
            }
        }
    }


    val userType = sharedPreferenceUseCase.getUser()
    val isAdmin = sharedPreferenceUseCase.getIsAdmin()

    private val _providerInitialTabIndex = MutableStateFlow(decideInitialTab())
    val providerInitialTabIndex: StateFlow<Int> = _providerInitialTabIndex

    private fun decideInitialTab(): Int =
        if (hasOngoingJobs()) 1 else 0

    private fun hasOngoingJobs(): Boolean = false

    fun setProviderInitialTab(index: Int) {
        _providerInitialTabIndex.value = index
    }

    fun resetProviderInitialTabToZero() {
        _providerInitialTabIndex.value = 0
    }

    private val _fromBookingsHandled = MutableStateFlow(false)
    val fromBookingsHandled = _fromBookingsHandled.asStateFlow()
    fun handleFromBookingsNavigation() {
        onNavItemClicked(1)
        _fromBookingsHandled.value = true
    }

    private val _fromServicesHandled = MutableStateFlow(false)
    val fromServicesHandled = _fromServicesHandled.asStateFlow()
    fun handleFromServicesNavigation() {
        onNavItemClicked(3)
        _fromServicesHandled.value = true
    }

    private val _fromMessageHandled = MutableStateFlow(false)
    val fromMessageHandled = _fromMessageHandled.asStateFlow()
    fun handleFromMessageNavigation() {
        onNavItemClicked(2)
        _fromBookingsHandled.value = true
    }

}