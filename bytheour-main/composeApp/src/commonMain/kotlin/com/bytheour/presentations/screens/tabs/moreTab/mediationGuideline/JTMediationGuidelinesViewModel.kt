package com.bytheour.presentations.screens.tabs.moreTab.mediationGuideline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.useCases.GeneralUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class JTMediationGuidelinesViewModel(
    private val generalUseCases: GeneralUseCases
) : ViewModel() {

    data class MediationGuidelineUIState(
        val introText: String = "",
        val guidelines: List<Guideline> = emptyList(),
        val message: String = ""
    )

    data class Guideline(
        val title: String,
        val description: String
    )

    private val _uiState = MutableStateFlow(MediationGuidelineUIState())
    val uiState: StateFlow<MediationGuidelineUIState> = _uiState.asStateFlow()

    fun getMediationGuidelines() {
        viewModelScope.launch {
            when (val response = generalUseCases.getMediationGuideline()) {
                is Success -> {
                    val guidelinesResponse = response.data?.mediationGuidelines.orEmpty()

                    // Extract intro text (first item with empty title but non-empty description)
                    val introText = guidelinesResponse
                        .firstOrNull { it.title.isNullOrBlank() && !it.description.isNullOrBlank() }
                        ?.description.orEmpty()

                    // Extract actual guidelines
                    val guidelines = guidelinesResponse.mapNotNull { item ->
                        val title = item.title
                        val description = item.description
                        if (!title.isNullOrBlank() && !description.isNullOrBlank()) {
                            Guideline(
                                title = title,
                                description = description
                            )
                        } else {
                            null
                        }
                    }

                    _uiState.value = _uiState.value.copy(
                        introText = introText,
                        guidelines = guidelines,
                        message = response.data?.meta?.message?:""
                    )
                }

                is Failure -> {
                    _uiState.value = _uiState.value.copy(
                        message = response.error.message
                    )
                }
            }
        }
    }

}
