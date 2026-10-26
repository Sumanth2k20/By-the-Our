package com.bytheour.presentations.screens.tabs.moreTab.reportAnIssue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.useCases.CommonUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JTReportAnIssueScreenViewModel(
    private val commonUseCases: CommonUseCases,
): ViewModel() {

    data class JTReportAnIssueScreenUiState(
        val issueType: String? = null,
        val issueDescription: String? = null,
        val issueTypeError: String? = null,
        val issueDescriptionError: String? = null,
        val message: String? = null
    )

    private val _uiState = MutableStateFlow(JTReportAnIssueScreenUiState())
    val uiState: StateFlow<JTReportAnIssueScreenUiState> = _uiState.asStateFlow()

    fun onIssueTypeChange(issueType: String) {
        _uiState.value = _uiState.value.copy(issueType = issueType, issueTypeError = null)
    }

    fun onIssueDescriptionChange(issueDescription: String) {
        _uiState.value = _uiState.value.copy(issueDescription = issueDescription, issueDescriptionError = null)
    }
    fun clearFields(){
        _uiState.value  = _uiState.value.copy(issueType = null, issueDescription = null)
    }


    fun validateInputs(): Boolean {
        var isValid = true

        val currentState = _uiState.value

        val issueTypeError = if (currentState.issueType.isNullOrBlank()) {
            isValid = false
            "Please Enter the Subject"
        } else null

        val issueDescriptionError = if (currentState.issueDescription.isNullOrBlank()) {
            isValid = false
            "Please Describe Your Issue"
        } else null

        _uiState.value = currentState.copy(
            issueTypeError = issueTypeError,
            issueDescriptionError = issueDescriptionError
        )

        return isValid
    }


    fun sendReportAnIssue(
        subject: String,
        description: String,
        onReportSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val result = commonUseCases.sendReport(
                subject = subject,
                description = description
            )
            when (result) {
                is Success -> {
                    if (result.data.data != null){
                        val message = result.data.meta.message
                        _uiState.value = _uiState.value.copy(message = message)
                    }else{
                        val message = result.data.meta.message
                        _uiState.value = _uiState.value.copy(message = message)
                        clearFields()
                        onReportSuccess()
                    }

                }

                is Failure -> {
                    val errorMessage = result.error.message
                    _uiState.value = _uiState.value.copy(message = errorMessage)
                }
            }
        }
    }
}