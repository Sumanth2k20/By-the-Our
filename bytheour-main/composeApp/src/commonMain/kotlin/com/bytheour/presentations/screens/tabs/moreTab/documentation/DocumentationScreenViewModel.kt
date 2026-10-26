package com.bytheour.presentations.screens.tabs.moreTab.documentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DocumentationScreenViewModel(
    private val commonUseCases: CommonUseCases,
): ViewModel() {

    data class Document(
        val file: String,
        val description: String
    )

    data class DocumentationScreenUiState(
        val documents: List<Document> = emptyList(),
        val message: String? = null
    )


    private val _uiState = MutableStateFlow(DocumentationScreenUiState())
    val uiState: StateFlow<DocumentationScreenUiState> = _uiState


    fun fetchDocuments() {
        viewModelScope.launch {
            when (val result = commonUseCases.getDocument()) {
                is JtApiResult.Success -> {
                    val docs = result.data.data.map {
                        Document(
                            file = it.file,
                            description = it.name
                        )
                    }
                    _uiState.value = _uiState.value.copy(
                        documents = docs,
                        message = null,
                    )
                }
                is JtApiResult.Failure -> {
                    _uiState.value = _uiState.value.copy(
                        message = result.error.message,
                    )
                }
            }
        }
    }

}


