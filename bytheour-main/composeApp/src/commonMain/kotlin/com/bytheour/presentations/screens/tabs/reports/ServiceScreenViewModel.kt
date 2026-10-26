package com.bytheour.presentations.screens.tabs.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.AdminUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ServiceScreenViewModel(
    private val adminUseCases: AdminUseCases
):ViewModel() {

    data class ServiceRequestUiState(
        val serviceRequestCount: Int? = null,
        val serviceManageCount: Int? = null,
        val message: String? = null,
    )

    private val _uiState = MutableStateFlow(ServiceRequestUiState())
    val uiState: StateFlow<ServiceRequestUiState> = _uiState.asStateFlow()


    fun getAdminServiceRequest(
        limit: String,
        offset: String,
    ) {
        viewModelScope.launch {
            val request = ServiceListRequest(
                limit = limit,
                offset = offset,
            )

            when (val result = adminUseCases.getAdminServiceRequest(request)) {
                is Success -> {
                    val data = result.data
                    _uiState.update { currentState ->
                        currentState.copy(
                            serviceRequestCount = data?.data?.total
                        )
                    }
                }
                is Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(message = result.error.message)
                    }
                }

            }
        }
    }


    fun getAdminServiceManage(
        limit: String,
        offset: String,
    ) {
        viewModelScope.launch {
            val request = ServiceListRequest(
                limit = limit,
                offset = offset,
            )

            when (val result = adminUseCases.getAdminServiceManage(request)) {
                is Success -> {
                    val data = result.data
                    _uiState.update { currentState ->
                        currentState.copy(
                            serviceManageCount = data?.data?.total
                        )
                    }
                }
                is Failure -> {
                    _uiState.update { currentState ->
                        currentState.copy(message = result.error.message)
                    }
                }
            }
        }
    }

}