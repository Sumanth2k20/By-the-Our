package com.bytheour.presentations.screens.tabs.reports


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.useCases.AdminUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JtDownLoadsScreenViewModel(
    private val adminUseCases: AdminUseCases
): ViewModel() {
    data class UiState(
        val iosDownload: Int? = null,
        val androidDownload: Int? = null,
        val message: String? = null,
    )
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()


    init {
        fetchDownLoadsCount()
    }

    private fun fetchDownLoadsCount() {
        viewModelScope.launch {
            when (val result = adminUseCases.getAdminDownloadCount()) {
                is Success -> {
                    val data = result.data
                    _uiState.update { currentState ->
                        currentState.copy(
                            iosDownload = data?.data?.iosDownloads?.toInt(),
                            androidDownload = data?.data?.androidDownloads?.toInt(),

                        )
                    }
                }

                is Failure-> {
                    _uiState.update { currentState ->
                        currentState.copy(message = result.error.message)
                    }
                }

            }
        }
    }
}