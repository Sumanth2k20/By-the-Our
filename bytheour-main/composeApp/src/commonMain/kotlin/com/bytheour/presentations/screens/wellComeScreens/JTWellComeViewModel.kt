package com.bytheour.presentations.screens.wellComeScreens

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


data class UiState(
    val currentPage: Int = 0,
)

class JTWelcomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiStates = _uiState.asStateFlow()

    fun onNextPage(totalPages: Int) {
        val nextPage = _uiState.value.currentPage + 1
        if (nextPage < totalPages) {
            _uiState.value = _uiState.value.copy(currentPage = nextPage)
        }
    }


}
