package com.bytheour.presentations.screens

import androidx.lifecycle.ViewModel
import com.bytheour.core.JTURLS.ROLETYPE
import com.bytheour.useCases.SharedPreferenceUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UiState(
    val selectedIndex: Int? = null,
    val selectedUserType:String?= null
)

class JTUserTypeViewModel(
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateSelectedType(userType: String) {
        _uiState.update { currentState ->
            currentState.copy(selectedUserType = userType)
        }
        sharedPreferenceUseCase.putUser(userType)
        ROLETYPE = sharedPreferenceUseCase.getUser()
        println("roletype ------------------------- $ROLETYPE")
    }




}

