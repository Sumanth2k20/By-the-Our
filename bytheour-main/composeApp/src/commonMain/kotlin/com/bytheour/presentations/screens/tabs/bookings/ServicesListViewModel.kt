package com.bytheour.presentations.screens.tabs.bookings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ServicesListViewModel:ViewModel() {

    private val _uiState = MutableStateFlow(
        listOf(
            "Lawn Aeration", "Lawn Treatment", "Lawn Landscaping", "Lawn Treatment",
            "Lawn Maintenance", "Lawn Irrigation Services", "Lawn Repairs", "Lawn Aeration",
        )
    )
    val uiState: StateFlow<List<String>> = _uiState

}