package com.bytheour.presentations.screens.tabs.explore.notification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.useCases.CommonUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

data class NotificationItem(
    val id: String,
    val type: String, // e.g., "Booking Reminder", "Booking Quotation"
    val message: String,
    val timeAgo: String,
    val imageUrl: String? = null, // optional
    val timestamp: Long,
)

class NotificationListScreenViewModel(
    private val commonUseCases: CommonUseCases
) : ViewModel() {

    data class NotificationUiState(
        val notifications: List<NotificationItem> = emptyList(),
        val isLoading: Boolean = false,
        val endReached: Boolean = false
    )

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState: StateFlow<NotificationUiState> = _uiState.asStateFlow()

    private var currentOffset = 0
    private val limit = 10

    init {
        loadNotifications()
    }

    fun loadNotifications() {
        if (_uiState.value.isLoading || _uiState.value.endReached) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            when (val result = commonUseCases.getNotificationList(limit.toString(), currentOffset.toString())) {
                is JtApiResult.Success -> {
                    val newItems = result.data.data.map { item ->
                        NotificationItem(
                            id = item.id,
                            type = when {
                                item.title.contains("Reminder", ignoreCase = true) -> "Booking Reminder"
                                item.title.contains("Accepted", ignoreCase = true) -> "Booking Accepted"
                                else -> "Booking Quotation"
                            },
                            message = item.message,
                            timeAgo = item.date.ifEmpty { "1 week ago" },
                            imageUrl = item.image,
                            timestamp = parseTimeAgoToTimestamp(item.date),
                        )
                    }


                    currentOffset += limit

                    _uiState.update {
                        it.copy(
                            notifications = it.notifications + newItems,
                            isLoading = false,
                            endReached = newItems.isEmpty()
                        )
                    }
                }

                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }
    private fun parseTimeAgoToTimestamp(timeAgo: String?): Long {
        if (timeAgo.isNullOrEmpty()) return Clock.System.now().toEpochMilliseconds()

        val now = Clock.System.now()

        return when {
            timeAgo.contains("hr", ignoreCase = true) -> {
                val hours = timeAgo.filter { it.isDigit() }.toIntOrNull() ?: 0
                val durationMillis = hours * 60 * 60 * 1000L
                Instant.fromEpochMilliseconds(now.toEpochMilliseconds() - durationMillis).toEpochMilliseconds()
            }
            timeAgo.contains("day", ignoreCase = true) -> {
                val days = timeAgo.filter { it.isDigit() }.toIntOrNull() ?: 0
                val durationMillis = days * 24 * 60 * 60 * 1000L
                Instant.fromEpochMilliseconds(now.toEpochMilliseconds() - durationMillis).toEpochMilliseconds()
            }
            else -> now.toEpochMilliseconds()
        }
    }
    fun categorizeDate(timestamp: Long): String {
        val instant = Instant.fromEpochMilliseconds(timestamp)

        return when {
            isToday(instant) -> "Today"
            isYesterday(instant) -> "Yesterday"
            else -> formatDate(instant)
        }
    }

    private fun isToday(instant: Instant): Boolean {
        val todayStart = getStartOfDay(Clock.System.now())
        val todayEnd = todayStart.plus(24 * 60 * 60 * 1000L - 1, DateTimeUnit.MILLISECOND)

        return instant >= todayStart && instant <= todayEnd
    }

    private fun isYesterday(instant: Instant): Boolean {
        val yesterdayStart = getStartOfDay(Clock.System.now().minus(24 * 60 * 60 * 1000L, DateTimeUnit.MILLISECOND))
        val yesterdayEnd = yesterdayStart.plus(24 * 60 * 60 * 1000L - 1, DateTimeUnit.MILLISECOND)

        return instant >= yesterdayStart && instant <= yesterdayEnd
    }

    private fun getStartOfDay(instant: Instant): Instant {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        val startOfDay = LocalDateTime(
            year = localDateTime.year,
            monthNumber = localDateTime.monthNumber,
            dayOfMonth = localDateTime.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )
        return startOfDay.toInstant(TimeZone.currentSystemDefault())
    }

    private fun formatDate(instant: Instant): String {
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
        return "${localDateTime.dayOfMonth.toString().padStart(2, '0')}-" +
                "${localDateTime.monthNumber.toString().padStart(2, '0')}-" +
                "${localDateTime.year}"
    }
}
