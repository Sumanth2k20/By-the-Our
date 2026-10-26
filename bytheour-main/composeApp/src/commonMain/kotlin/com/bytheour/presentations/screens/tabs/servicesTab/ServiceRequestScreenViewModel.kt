package com.bytheour.presentations.screens.tabs.servicesTab


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.ServiceByWorkerRequest
import com.bytheour.useCases.ProviderServicesUseCases
import com.bytheour.utils.toPascalCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ServiceRequestScreenViewModel(
    private val providerUseCase: ProviderServicesUseCases,
): ViewModel() {

    data class JTReportAnIssueScreenUiState(
        val service: String? = null,
        val serviceDescription: String? = null,
        val serviceError: String? = null,
        val serviceDescriptionError: String? = null,
        val message: String? = null
    )

    private val _uiState = MutableStateFlow(JTReportAnIssueScreenUiState())
    val uiState: StateFlow<JTReportAnIssueScreenUiState> = _uiState.asStateFlow()

    fun onServiceChange(service: String) {
        _uiState.value = _uiState.value.copy(service = service, serviceError = null)
    }

    fun onServiceDescriptionChange(serviceDescription: String) {
        _uiState.value = _uiState.value.copy(serviceDescription = serviceDescription, serviceDescriptionError = null)
    }
    fun clearFields(){
        _uiState.value  = _uiState.value.copy(service = null, serviceDescription = null, message = null)
    }


    fun validateInputs(): Boolean {
        var isValid = true

        val currentState = _uiState.value

        val issueTypeError = if (currentState.service.isNullOrBlank()) {
            isValid = false
            "Please enter service title"
        } else null

        val issueDescriptionError = if (currentState.serviceDescription.isNullOrBlank()) {
            isValid = false
            "Please enter service description"
        } else null

        _uiState.value = currentState.copy(
            serviceError = issueTypeError,
            serviceDescriptionError = issueDescriptionError
        )

        return isValid
    }

    fun addNewService(
        onSuccess:()->Unit,
        catId:String
    ){
        viewModelScope.launch {
            val result = providerUseCase.serviceByWorker(
                ServiceByWorkerRequest(
                    category_id = catId,
                    title = _uiState.value.service?.toPascalCase(),
                    description = _uiState.value.serviceDescription?.toPascalCase()
                )
            )
            println("titlePascalCase =================== ${_uiState.value.service?.toPascalCase()}")
            println("descriptionPascalCase =================== ${_uiState.value.serviceDescription?.toPascalCase()}")

            when(result){
                is Success -> {
                    onSuccess()
                    _uiState.value = _uiState.value.copy(message = result.data.meta.message)
                }
                is Failure -> {
                    _uiState.value = _uiState.value.copy(message = result.error.message)
                }
            }
        }
    }

}