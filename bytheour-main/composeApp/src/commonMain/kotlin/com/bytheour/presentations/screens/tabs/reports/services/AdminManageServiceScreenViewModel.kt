package com.bytheour.presentations.screens.tabs.reports.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.useCases.AdminUseCases
import com.bytheour.utils.PAGINATION_LIMIT_15
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AdminUserUiState(
    val deviceType: String?= "--",
    val totalCount :Int?= null,
    val contactList: List<ContactCardUiState>? = emptyList(),
    val message: String? = null,
    val selectAll: Boolean = false,
    val search: String? = "",
    val hasMoreData: Boolean = true,
    val currentPage: Int = 0,
    val isLoadingMore: Boolean = false
)

data class ContactCardUiState(
    val id: String,
    val isSelected: Boolean,
    val isStatusActive: Boolean,
    val description: String? = "",
    val categoryName: String? = "",
    val sequence: Int? = 0,
    val title: String? = "",
    val profile: String,
    val image: String? = null,
    val serviceStatus: String? = "",
    val categoryId: Int? = 0
)

class AdminManageServiceScreenViewModel(
    private val adminUseCases: AdminUseCases
): ViewModel() {


    private val _uiState = MutableStateFlow(AdminUserUiState())
    val uiStates = _uiState.asStateFlow()


    private var searchJob: Job? = null

    fun updateSearch(search: String) {
        _uiState.update {
            it.copy(search = search)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            performSearch(search)
        }
    }

    private fun performSearch(searchQuery: String) {
        _uiState.update {
            it.copy(
                contactList = emptyList(),
                currentPage = 0,
                hasMoreData = true
            )
        }
        getServiceList(isRefresh = false, searchQuery = searchQuery)
    }

    private fun getServiceList(
        isRefresh: Boolean = false,
        searchQuery: String? = null
    ) {
        val currentState = _uiState.value

        if (currentState.isLoadingMore) return

        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage
            val offset = (page * PAGINATION_LIMIT_15).toString()

            val keyword = when {
                searchQuery != null -> {
                    val trimmed = searchQuery.trim()
                    if (trimmed.length >= 3) trimmed else null
                }
                currentState.search?.isNotEmpty() == true -> {
                    val trimmed = currentState.search.trim()
                    if (trimmed.length >= 3) trimmed else null
                }
                else -> null
            }

            _uiState.update { it.copy(isLoadingMore = true) }

            val result = adminUseCases.getServicesList(
                limit = PAGINATION_LIMIT_15.toString(),
                offset = offset,
                keyword = keyword
            )

            when (result) {
                is Success -> {
                    val records = result.data.data?.records
                    val newItems = records?.map { record ->
                        ContactCardUiState(
                            id = record?.id?.toString() ?: "",
                            isSelected = record?.isSelected == true,
                            isStatusActive = record?.isActive == "y",
                            categoryName = record?.category,
                            sequence = record?.sequence?:0,
                            description = record?.description,
                            title = record?.title,
                            profile = record?.profile?:"",
                            serviceStatus = record?.isActive?:"",
                            image = record?.image?:"",
                            categoryId = record?.categoryId
                        )
                    } ?: emptyList()

                    val totalCount = result.data.data?.total ?: 0
                    val currentItems = if (isRefresh) emptyList() else (currentState.contactList ?: emptyList())
                    val updatedItems = currentItems + newItems
                    val hasMore = updatedItems.size < totalCount

                    _uiState.update { state ->
                        state.copy(
                            contactList = updatedItems,
                            totalCount = totalCount,
                            currentPage = page + 1,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            message = null
                        )
                    }
                }
                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }


    fun loadMoreData() {
        getServiceList(isRefresh = false)
    }

    fun refreshData() {
        _uiState.update { it.copy(search = "") }
        getServiceList(isRefresh = true)
    }


    fun serviceStatusChange(
        id: String,
        status: String
    ) {
        viewModelScope.launch {

            when (val result = adminUseCases.changeServiceStatus(id = id,status = status)) {
                is Success -> {
                    _uiState.update { currentState ->
                        val updatedList = currentState.contactList?.map {
                            if (it.id == id) it.copy(
                                isStatusActive = status == "y",
                            )
                            else it
                        }
                        currentState.copy(contactList = updatedList)
                    }
                }

                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun adminDeleteService(id: String) {
        viewModelScope.launch {
            when (val result = adminUseCases.adminDeleteService(id)) {
                is Success -> {
                    _uiState.update { currentState ->
                        val updatedList = currentState.contactList?.filter { it.id != id }
                        val newTotalCount = (currentState.totalCount ?: 0) - 1
                        currentState.copy(
                            contactList = updatedList,
                            totalCount = newTotalCount
                        )
                    }
                }
                is Failure -> {
                    _uiState.update { state ->
                        state.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

    fun clearMessage() {
        _uiState.update { it.copy(message = null) }
    }
}