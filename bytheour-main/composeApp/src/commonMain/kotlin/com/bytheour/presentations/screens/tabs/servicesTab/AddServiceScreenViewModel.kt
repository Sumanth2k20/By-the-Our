package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.model.response.ModelGallery
import com.bytheour.data.model.response.ServerImages
import com.bytheour.data.request.DeleteServiceRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.GalleryUseCases
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.useCases.ProviderServicesUseCases
import com.bytheour.utils.PAGINATION_LIMIT_30
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddServiceScreenViewModel (
    private val providerBookingUseCases: ProviderBookingUseCases,
    private val providerServicesUseCases: ProviderServicesUseCases,
    private val imageUploadViewModel: ImageUploadViewModel,
    private val galleryUseCases: GalleryUseCases
) : ViewModel() {

    data class AddServiceUiState(
        val hourlyRate: String? = null,
        val hourlyRateError: String? = null,
        val minHours: Int? = null,
        val minHoursError: String? = null,
        val experience: String? = null,
        val experienceError: String? = null,
        val description: String? = null,
        val isPremiseAvailable: Boolean? = null,
        val premiseError: String? = null,
        val radiusError: String? = null,
        val radiusMiles: String? = null,
        val message:String? = null,
        val userNme:String? = null,
        val profile:String? = null,
        val galleryImages: ArrayList<ServerImages>? = null,
        val imagePreviews: List<ImageBitmap> = emptyList(),
        val pendingUploads: List<ByteArray> = emptyList(),
        val workerServiceId:String? = null,
        val galleryBookings: List<ModelGallery?> = emptyList(),
        val serviceId:String? = null,

        )

    private val _uiState = MutableStateFlow(AddServiceUiState())
    val uiState: StateFlow<AddServiceUiState> = _uiState.asStateFlow()

    fun fetchGalleryBookings() {
        viewModelScope.launch {
            val result = galleryUseCases.getGalleryBookings()

            when (result) {
                is JtApiResult.Success -> {
                    _uiState.update { current ->
                        current.copy(
                            galleryBookings = result.data.data
                        )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update { current ->
                        current.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }




    fun updateHourlyRate(rate: String) {
        val error = when {
            rate.isBlank() -> "Hourly Rate Cannot Be Empty"
            rate.toFloatOrNull() == null -> "Hourly Rate Must Be a Number"
            rate.toFloatOrNull() != null && rate.toFloat() <= 0f -> "Hourly Rate Rust Be Greater Than 0"
            else -> null
        }

        _uiState.update { it.copy(hourlyRate = rate, hourlyRateError = error) }
    }

    fun pendingUploads(pendingUploads: List<ByteArray>) {
        _uiState.update { it.copy(pendingUploads = pendingUploads) }
    }
    fun imagePreviews(imagePreviews: List<ImageBitmap>) {
        _uiState.update { it.copy(imagePreviews = imagePreviews) }
    }

    fun updateMinHours(minHours: Int) {
        _uiState.update { it.copy(minHours = minHours, minHoursError = null) }
    }

    fun updateExperience(exp: String) {
        _uiState.update { it.copy(experience = exp, experienceError = null) }
    }

    fun updateDescription(desc: String) {
        _uiState.update { it.copy(description = desc) }
    }

    fun updatePremiseAvailable(isAvailable: Boolean) {
        _uiState.update { it.copy(isPremiseAvailable = isAvailable, premiseError = null) }
    }

    fun updateRadius(radius: String) {
        _uiState.update { it.copy(radiusMiles = radius, radiusError = null) }
    }




    fun submitService(
        serviceId: String,
        onSuccess: () -> Unit,
        pendingUploads: List<ByteArray>
    ) {
        viewModelScope.launch {
            if (!validateForm()) return@launch
            val ui = _uiState.value

            // Build the request map
            val options = buildMap {
                ui.minHours?.let { put("service[$serviceId][min_hour]", it.toString()) }
                ui.hourlyRate?.let { put("service[$serviceId][rate]", it) }
                ui.description?.takeIf { it.isNotBlank() }?.let {
                    put("service[$serviceId][description]", it)
                }
                ui.radiusMiles?.takeIf { it.isNotBlank() }?.let {
                    put("service[$serviceId][radius]", it)
                }
                ui.experience?.takeIf { it.isNotBlank() }?.let {
                    put("service[$serviceId][experience]", it)
                }
                ui.isPremiseAvailable?.let {
                    put("service[$serviceId][service_at_premises]", if (it) "y" else "n")
                }
            }

            val result = providerServicesUseCases.addService(options)

            when (result) {
                is JtApiResult.Success -> {

                    if (result.data.meta.message?.contains("successfully", ignoreCase = true) == true){
                        _uiState.update {
                            it.copy(
                                workerServiceId = result.data.data,
                                message = result.data.meta.message
                            )
                        }
                        imageUploadViewModel.saveAllImages(
                            result.data.data?:"",
                            onSuccess = {onSuccess()},
                            uploads = pendingUploads
                            )

                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

    fun clearError() {
        _uiState.update {
            it.copy(
            message = null
            )
        }
    }

    private fun validateForm(): Boolean {
        val ui = _uiState.value

        var isValid = true
        var hourlyRateError: String? = null
        var radiusError: String? = null
        var premiseError: String? = null
        var minHoursError: String? = null
        var experienceError: String? = null

        val rate = ui.hourlyRate ?: ""
        hourlyRateError = when {
            rate.isBlank() -> {
                isValid = false
                "Hourly rate cannot be empty"
            }
            rate.toFloatOrNull() == null -> {
                isValid = false
                "Hourly rate must be a number"
            }
            rate.toFloat() <= 0f -> {
                isValid = false
                "Hourly rate must be greater than 0"
            }
            rate.toFloat() <= 30f -> {
                isValid = false
                "Hourly rate must be greater than $30"
            }
            else -> null
        }

        val exp = ui.experience ?: ""
        experienceError = when {
            exp.isBlank() -> {
                isValid = false
                "Experience cannot be empty"
            }
            exp.toFloatOrNull() == null -> {
                isValid = false
                "Experience must be a number"
            }
            exp.toFloat() <= 0f -> {
                isValid = false
                "Experience must be greater than 0"
            }
            exp.toFloat() > 50 -> {
                isValid = false
                "Experience cannot be more than 50 years"
            }
            else -> null
        }

        radiusError = when {
            ui.radiusMiles.isNullOrBlank() || ui.radiusMiles.toFloatOrNull() == null -> {
                isValid = false
                "Radius must be a valid number"
            }
            ui.radiusMiles.toFloat() <= 0f -> {
                isValid = false
                "Radius must be greater than 0"
            }
            else -> null
        }

        if (ui.isPremiseAvailable == null) {
            premiseError = "Please select if the service is available at your premises"
            isValid = false
        }

        minHoursError = when {
            ui.minHours == null -> {
                isValid = false
                "Minimum hours is required"
            }
            ui.minHours < 1 -> {
                isValid = false
                "Minimum hours must be at least 1"
            }
            else -> null
        }

        _uiState.update {
            it.copy(
                hourlyRateError = hourlyRateError,
                radiusError = radiusError,
                premiseError = premiseError,
                minHoursError = minHoursError,
                experienceError = experienceError
            )
        }

        return isValid
    }





    fun loadServices(serviceId: String) {
        viewModelScope.launch {
            val request = ServiceListRequest(
                limit  = PAGINATION_LIMIT_30,
                offset = "0"
            ) // we are giving hard coded because of inside the list we need to compare the id

            when (val result = providerBookingUseCases.getService(request)) {
                is JtApiResult.Success -> {
                    val serviceData = result.data.data.firstOrNull {
                        it.service.id== serviceId
                    }
                    if (serviceData != null) {
                        _uiState.update { current ->
                            current.copy(
                                galleryImages = serviceData.images ?: arrayListOf(),
                                serviceId = serviceData.service.id
                            )
                        }
                    }
                }

                is JtApiResult.Failure -> _uiState.update {
                    it.copy(
                        message = "Failed to load services"
                    )
                }
            }
        }
    }



    fun deleteService(
        serviceId: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {

            val result = providerServicesUseCases.deleteService(
                DeleteServiceRequest(service = serviceId)
            )

            when (result) {
                is JtApiResult.Success -> {
                    onSuccess()
                    _uiState.update {
                        it.copy(message = "Service deleted successfully")
                    }

                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }

}