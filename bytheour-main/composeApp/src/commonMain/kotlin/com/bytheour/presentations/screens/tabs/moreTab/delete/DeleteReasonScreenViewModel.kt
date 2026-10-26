package com.bytheour.presentations.screens.tabs.moreTab.delete

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeleteScreenViewModel(
    private val commonUseCases: CommonUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
): ViewModel() {

    data class DeleteScreenUiState(
        val reason: String? = null,
        val reasonError: String? = null,
        val message: String? = null
    )

    private val _uiState = MutableStateFlow(DeleteScreenUiState())
    val uiState: StateFlow<DeleteScreenUiState> = _uiState.asStateFlow()


    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }

    fun deleteAccount(
        reason: String,
        onSuccess: () -> Unit,
    ){
        viewModelScope.launch {
            when(val result = commonUseCases.Delete(reason)){
                is JtApiResult.Success ->{
                    _uiState.update {
                        it.copy(message = result.data.meta.message)
                    }
                    onSuccess()
                }
                is JtApiResult.Failure ->{
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }

    }


    fun onIssueDescriptionChange(issueDescription: String) {
        _uiState.value =
            _uiState.value.copy(reason = issueDescription, reasonError = null)
    }


    fun validateInputs(): Boolean {
        var isValid = true

        val currentState = _uiState.value

        val issueDescriptionError = if (currentState.reason.isNullOrBlank()) {
            isValid = false
            "Please describe your Reason"
        } else null

        _uiState.value = currentState.copy(
            reasonError = issueDescriptionError
        )

        return isValid
    }
}
