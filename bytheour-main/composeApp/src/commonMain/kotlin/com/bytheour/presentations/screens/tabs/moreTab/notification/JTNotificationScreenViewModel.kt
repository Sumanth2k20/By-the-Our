package com.bytheour.presentations.screens.tabs.moreTab.notification

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update


class JTNotificationScreenViewModel : ViewModel() {

    data class NotificationUIState(
        val inboxMessages: Boolean = true,
        val ratingsReminders: Boolean = false,
        val promotionsAndTips: Boolean = true,
        val accountUpdates: Boolean = false
    )

    private val _uiState = MutableStateFlow(NotificationUIState())
    val uiState: StateFlow<NotificationUIState> = _uiState.asStateFlow()

    fun toggleInboxMessages(enabled: Boolean) {
        _uiState.update { it.copy(inboxMessages = enabled) }
    }

    fun toggleRatingsReminders(enabled: Boolean) {
        _uiState.update { it.copy(ratingsReminders = enabled) }
    }

    fun togglePromotionsAndTips(enabled: Boolean) {
        _uiState.update { it.copy(promotionsAndTips = enabled) }
    }

    fun toggleAccountUpdates(enabled: Boolean) {
        _uiState.update { it.copy(accountUpdates = enabled) }
    }
}
