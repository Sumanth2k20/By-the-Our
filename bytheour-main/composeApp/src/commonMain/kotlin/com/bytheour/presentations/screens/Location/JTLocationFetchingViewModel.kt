package com.bytheour.presentations.screens.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.GeneralUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTLocationFetchingViewModel(
    private val generalUseCases: GeneralUseCases,
):ViewModel() {

    data class LocationUiSate(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val flatNo: String? = "",
        val address: String? = "",
        val landmark: String?= "",
        val pinCode: String? = "",
        val country: String? = "",
        val hasPermissionDenied:Boolean = false,
        val deviceLocationAccess:Boolean = false,
        val isLocationFetch : Boolean = true,
        val message:String = ""
    )

    private val _uiState = MutableStateFlow(LocationUiSate())
    val uiState: StateFlow<LocationUiSate> = _uiState.asStateFlow()

    fun updateFullAddress(flatNo: String?, address: String?, landmark: String?, pinCode: String?,country:String?,latitude: Double?,longitude: Double?) {
        _uiState.update { state ->
            state.copy(
                flatNo = flatNo,
                address = address?.let { if (pinCode != null) "$it $pinCode" else it },
                landmark = landmark,
                country = country,
                latitude = latitude ?: 0.0,
                longitude = longitude ?: 0.0
            )
        }
    }

    fun deviceToken(
        device: String,
        token: String,
        type: String,
        version: Int,
        os: String,
        appVersion: String
    ) {
        viewModelScope.launch {
            val result = generalUseCases.deviceToken(
                device = device,
                token = token,
                type = type,
                version = version,
                os = os,
                appVersion = appVersion,
            )

            when (result) {
                is JtApiResult.Success -> {
                    _uiState.update { it.copy(message = result.data.meta.message ?: "Token saved") }
                }
                is JtApiResult.Failure -> {
                    _uiState.update { it.copy(message = result.error.message.ifEmpty { "Failed to save token" }) }
                }
            }
        }
    }


}