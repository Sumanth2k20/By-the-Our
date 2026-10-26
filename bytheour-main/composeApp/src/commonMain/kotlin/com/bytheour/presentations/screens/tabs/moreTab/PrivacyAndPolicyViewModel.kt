package com.bytheour.presentations.screens.tabs.moreTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.GeneralUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PrivacyAndPolicyViewModel(
    private val generalUseCases: GeneralUseCases
):ViewModel() {

    data class PrivacyAndPolicyUiState(
        val policyText: String = "",
        val message: String = ""
    )


    private val _uiState = MutableStateFlow(PrivacyAndPolicyUiState())
    val uiState: StateFlow<PrivacyAndPolicyUiState> = _uiState.asStateFlow()



     fun getPrivacyAndPolicy(){
         viewModelScope.launch {
             when(val result = generalUseCases.getPrivacyAndPolicy()){
                 is JtApiResult.Success -> {
                     _uiState.value = _uiState.value.copy(
                         policyText = result.data?.content ?: "",
                         message = result.data?.meta?.message ?: ""
                     )
                 }
                 is JtApiResult.Failure -> {
                     _uiState.value = _uiState.value.copy(
                         message = result.error.message
                     )
                 }
             }
         }
    }


}