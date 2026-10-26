package com.bytheour.presentations.screens.tabs.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.UsersListRequest
import com.bytheour.useCases.ChatUseCases
import com.bytheour.useCases.SharedPreferenceUseCase
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.TYPE_CUSTOMER
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class JTChatListViewModel(
    private val chatUseCases: ChatUseCases,
    private val sharedPreferenceUseCase: SharedPreferenceUseCase
) : ViewModel() {

    data class ChatItemUI(
        val roomId: String?= "",
        val name: String?= "",
        val profile: String?= "",
        val message: String?= "",
        val date: String?= "",
        val isArchive: Boolean?= null
    )

    data class ChatListUiState(
        val chatList: List<ChatItemUI> = emptyList(),
        val hasMoreData: Boolean = true,
        val currentPage: Int = 0,
        val isLoadingMore: Boolean = false,
        val message: String? = null,
        val totalCount: Int = 0,
        val search: String? = "",
    )

    private val _chatList = MutableStateFlow<List<ChatItemUI>>(emptyList())
    val chatList: StateFlow<List<ChatItemUI>> = _chatList.asStateFlow()

    private val _uiState = MutableStateFlow(ChatListUiState())
    val uiState = _uiState.asStateFlow()

    private val _allChats = mutableListOf<ChatItemUI>()
    fun updateSearch(search: String){
        _uiState.update {
            it.copy(search = search)
        }
        applySearchFilter()
    }
    fun applySearchFilter() {
        val searchQuery = _uiState.value.search?.trim()?.lowercase().orEmpty()
        val filteredList = if (searchQuery.isEmpty()) {
            _allChats
        } else {
            _allChats.filter {
                it.name?.lowercase()?.contains(searchQuery) == true
            }
        }
        _chatList.value = filteredList
    }
     fun getChatRoomsList() {
        viewModelScope.launch {
            when (val result = chatUseCases.getChatList()) {
                is JtApiResult.Success -> {
                    val chatList = result.data?.data?.map {
                        ChatItemUI(
                            roomId = it.id,
                            name = if (sharedPreferenceUseCase.getUser()== TYPE_CUSTOMER) "${it.worker.firstName} ${it.worker.lastName}".trim() else "${it.user.firstName} ${it.user.lastName}".trim(),
                            profile = if (sharedPreferenceUseCase.getUser()== TYPE_CUSTOMER) it.worker.profile else it.user.profile,
                            date = it.date
                        )
                    }.orEmpty()
                    _allChats.clear()
                    _allChats.addAll(chatList)

                    // Apply current search filter immediately
                    applySearchFilter()

                }

                is JtApiResult.Failure -> {
                    _chatList.value = emptyList()
                }
            }
        }
    }


    private fun getArchivedChatList(
        limit: String = PAGINATION_LIMIT.toString(),
        isRefresh: Boolean = false
    ) {
        val currentState = _uiState.value
        if (currentState.isLoadingMore && !isRefresh) return

        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage
            val calculatedOffset = (page * limit.toInt()).toString()

            _uiState.update {
                it.copy(isLoadingMore = true)
            }

            val request = UsersListRequest(
                offset = calculatedOffset,
                limit = limit
            )

            when (val result = chatUseCases.getArchivedChat(request)) {
                is JtApiResult.Success -> {
                    val newChatList = result.data?.data?.map {
                        ChatItemUI(
                            roomId = it.id,
                            name = "${it.worker.firstName} ${it.worker.lastName}".trim(),
                            profile = it.worker.profile,
                            isArchive = true,
                            date = it.date
                        )
                    }.orEmpty()

                    val currentItems = if (isRefresh) emptyList() else _chatList.value
                    val updatedItems = currentItems + newChatList
                    val hasMore = newChatList.size >= limit.toInt()

                    _chatList.value = updatedItems
                    _uiState.update {
                        it.copy(
                            chatList = updatedItems,
                            totalCount = updatedItems.size,
                            currentPage = page + 1,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            message = null
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                    _chatList.value = emptyList()
                }
            }
        }
    }

    fun loadMoreArchivedChats(limit: String = PAGINATION_LIMIT.toString()) {
        getArchivedChatList(
            limit = limit,
            isRefresh = false
        )
    }

    fun refreshArchivedChats(limit: String = PAGINATION_LIMIT.toString()) {
        getArchivedChatList(
            limit = limit,
            isRefresh = true
        )
    }
}