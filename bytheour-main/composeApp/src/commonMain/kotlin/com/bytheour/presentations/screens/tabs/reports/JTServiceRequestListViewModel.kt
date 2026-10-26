package com.bytheour.presentations.screens.tabs.reports

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult.Failure
import com.bytheour.core.JtApiResult.Success
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.useCases.AdminUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class JTServiceRequestListViewModel(
    private val adminUseCases: AdminUseCases
):ViewModel() {

    data class ServiceRequestUiState(
        val totalCount: Int? = 0,
        val message: String? = null,
        val search: String? = "",
        val title: String? = "",
        val service: String? = "",
        val serviceCardUiState: List<ServiceCardUiState> = emptyList(),
        val hasMoreData: Boolean = true,
        val currentPage: Int = 0,
        val isLoadingMore: Boolean = false
    )

    data class ServiceCardUiState(
        val id: Int,
        val title: String? = "",
        val description: String? = "",
        val workerName: String? = "Details not Available",
        val category: String,
        val serviceStatus: String,
        val profile: String,
        val sequence: Int,
    )

    private val _uiState = MutableStateFlow(ServiceRequestUiState())
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
                serviceCardUiState = emptyList(),
                currentPage = 0,
                hasMoreData = true
            )
        }
        getAdminServiceRequest(isRefresh = false, searchQuery = searchQuery)
    }



    private fun getAdminServiceRequest(
        isRefresh: Boolean = false,
        searchQuery: String? = null
    ) {
        val currentState = _uiState.value
        if (currentState.isLoadingMore) return

        if (!currentState.hasMoreData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentPage
            val offset = (page * PAGINATION_LIMIT).toString()

            _uiState.update {
                it.copy(isLoadingMore = true)
            }

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

            val request = ServiceListRequest(
                limit = PAGINATION_LIMIT.toString(),
                offset = offset,
                keyWord = keyword
            )

            when (val result = adminUseCases.getAdminServiceRequest(request)) {
                is Success -> {
                    val data = result.data
                    val newItems = data?.data?.records?.map {
                        ServiceCardUiState(
                            id = it?.id ?: 0,
                            title = it?.title.orEmpty(),
                            description = it?.description.orEmpty(),
                            workerName = it?.workerName.orEmpty(),
                            category = it?.category.orEmpty(),
                            serviceStatus = it?.serviceStatus.orEmpty(),
                            profile = it?.image ?: "",
                            sequence = it?.sequence ?: 0
                        )
                    } ?: emptyList()

                    val totalCount = data?.data?.total ?: 0
                    val currentItems = if (isRefresh) emptyList() else currentState.serviceCardUiState
                    val updatedItems = currentItems + newItems
                    val hasMore = updatedItems.size < totalCount

                    _uiState.update {
                        it.copy(
                            totalCount = totalCount,
                            serviceCardUiState = updatedItems,
                            currentPage = page + 1,
                            hasMoreData = hasMore,
                            isLoadingMore = false,
                            message = null
                        )
                    }
                }
                is Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMore = false
                        )
                    }
                }
            }
        }
    }

    fun loadMoreData() {
        getAdminServiceRequest(isRefresh = false)
    }

    fun refreshData() {
        getAdminServiceRequest(isRefresh = true)
    }
}