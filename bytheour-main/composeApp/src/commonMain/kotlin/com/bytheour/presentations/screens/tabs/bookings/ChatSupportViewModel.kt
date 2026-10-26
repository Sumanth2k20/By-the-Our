package com.bytheour.presentations.screens.tabs.bookings

import androidx.compose.ui.graphics.ImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.AddSupportTicketRequest
import com.bytheour.data.request.GetSupportHistoryRequest
import com.bytheour.presentations.screens.tabs.messages.SupportHistoryMessage
import com.bytheour.useCases.CommonChatSupportTicketUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ChatSupportState(
    val message: String? = null,
    val supportId: String? = null,
    val id: String? = null,
    val text: String? = null,
    val isUser: Boolean? = null,
    val imageBitmap: ImageBitmap? = null,
    val hasMoreData: Boolean = true,
    val currentPage: Int = 0,
    val isLoadingMore: Boolean = false,
    val isInitialLoading: Boolean = false,
    val messages: List<SupportHistoryMessage> = emptyList(),
    val pageSize: Int = 10
)

class ChatSupportViewModel(
    private val commonChatSupport: CommonChatSupportTicketUseCases
): ViewModel() {

    private val _uiStates = MutableStateFlow(ChatSupportState())
    val uiStates = _uiStates.asStateFlow()

    fun addTicketSupport(bookingId: String) {
        val request = AddSupportTicketRequest(
            booking = bookingId
        )
        viewModelScope.launch {
            when(val result = commonChatSupport.addTicketSupport(request)){
                is JtApiResult.Success -> {
                    val data = result.data?.data
                    val meta = result.data?.meta

                    if (data != null) {
                        _uiStates.update { currentState ->
                            currentState.copy(
                                message = meta?.message,
                                supportId = data.id
                            )
                        }

                        // Automatically load first page of messages
                        loadInitialMessages(data.id)
                    } else {
                        _uiStates.update { currentState ->
                            currentState.copy(
                                message = meta?.message ?: "Invalid Booking Details",
                                supportId = null
                            )
                        }
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update { currentState ->
                        currentState.copy(
                            message = result.error.message,
                            supportId = null
                        )
                    }
                }
            }
        }
    }

    private fun loadInitialMessages(supportId: String) {
        viewModelScope.launch {
            _uiStates.update { it.copy(isInitialLoading = true) }
            getSupportHistory(supportId, isRefresh = true)
        }
    }

    fun loadMoreMessages() {
        val currentState = _uiStates.value
        currentState.supportId?.let { supportId ->
            if (!currentState.isLoadingMore && currentState.hasMoreData) {
                getSupportHistory(supportId, isLoadMore = true)
            }
        }
    }

    fun refreshMessages() {
        val currentState = _uiStates.value
        currentState.supportId?.let { supportId ->
            getSupportHistory(supportId, isRefresh = true)
        }
    }

    private fun getSupportHistory(
        roomId: String,
        isRefresh: Boolean = false,
        isLoadMore: Boolean = false
    ) {
        val currentState = _uiStates.value

        if (currentState.isLoadingMore && !isRefresh) return
        if (!currentState.hasMoreData && !isRefresh && !isLoadMore) return

        viewModelScope.launch {
            val page = when {
                isRefresh -> 0
                isLoadMore -> currentState.currentPage
                else -> 0
            }

            val offset = page * currentState.pageSize

            _uiStates.update { currentState ->
                currentState.copy(
                    isLoadingMore = isLoadMore || isRefresh,
                    isInitialLoading = if (isRefresh && currentState.messages.isEmpty()) true else currentState.isInitialLoading
                )
            }

            val request = GetSupportHistoryRequest(
                supportId = roomId,
                limit = currentState.pageSize.toString(),
                offset = offset.toString()
            )

            when (val result = commonChatSupport.getSupportHistory(request)) {
                is JtApiResult.Success -> {
                    val messagesResponse = result.data?.data ?: emptyList()

                    val newMessageList = messagesResponse.map { chatItem ->
                        SupportHistoryMessage(
                            id = chatItem.room ?: "",
                            text = chatItem.message ?: "",
                            isUser = chatItem.sender == "user",
                            timestamp = chatItem.time
                        )
                    }

                    val updatedMessages = when {
                        isRefresh -> newMessageList
                        isLoadMore -> currentState.messages + newMessageList
                        else -> newMessageList
                    }

                    val hasMore = newMessageList.size >= currentState.pageSize
                    val nextPage = if (isRefresh) 1 else currentState.currentPage + 1

                    _uiStates.update { currentState ->
                        currentState.copy(
                            supportId = roomId,
                            message = result.data?.meta?.message,
                            messages = updatedMessages,
                            currentPage = nextPage,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            isInitialLoading = false
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update { currentState ->
                        currentState.copy(
                            message = result.error.message,
                            isLoadingMore = false,
                            isInitialLoading = false
                        )
                    }
                }
            }
        }
    }
}