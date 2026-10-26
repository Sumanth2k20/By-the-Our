package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.SearchServiceRequest
import com.bytheour.data.request.ServiceListRequest
import com.bytheour.presentations.screens.tabs.servicesTab.ProviderSelectServiceScreenViewModel.Services
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.ProviderBookingUseCases
import com.bytheour.useCases.ProviderServicesUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CategoriesViewModel(
    private val providerServicesUseCases: ProviderServicesUseCases,
    private val providerBookingUseCases: ProviderBookingUseCases,
    private val generalUseCases: GeneralUseCases
): ViewModel() {

    data class Category(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = "",
        val dec:String? = ""

    )

    data class CategoryUiState(
        val services: List<Category> = emptyList(),
        val searchServices: List<Services> = emptyList(),
        val message: String? = null,
        val search: String? = "",
        val isLoadingMore: Boolean = false,
        val hasMoreData: Boolean = true,
        val paginationOffset: Int = 0
    )

    private val _uiState = MutableStateFlow(CategoryUiState())
    val uiState: StateFlow<CategoryUiState> = _uiState.asStateFlow()

    var searchJob: Job? = null

    fun updateSearch(text:String){
        _uiState.update { it.copy(search = text) }
    }

    fun updateSearchAndSearch(keyword: String) {
        val currentState = _uiState.value
        _uiState.update {
            it.copy(
                search = keyword,
                searchServices = emptyList(),
                paginationOffset = 0,
                hasMoreData = true
            )
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            getSearchServices(
                keyword = keyword.trim(),
                limit = PAGINATION_LIMIT.toString(),
                offset = currentState.paginationOffset.toString(),
                isInitialLoad = true
            )
        }
    }

    fun loadMoreSearchResults() {
        val currentState = _uiState.value
        if (!currentState.isLoadingMore && currentState.hasMoreData) {
            getSearchServices(
                keyword = currentState.search.orEmpty().trim(),
                limit = PAGINATION_LIMIT.toString(),
                offset = currentState.paginationOffset.toString(),
                isInitialLoad = false
            )
        }
    }

    fun getCategories() {
        viewModelScope.launch {
            val result = providerServicesUseCases.getCategory()
            when (result) {
                is JtApiResult.Success -> {
                    val categoryList = result.data.data.map { serviceList ->
                        Category(
                            title = serviceList.title,
                            description = serviceList.description,
                            image = serviceList.image,
                            id = serviceList.id,
                            dec = serviceList.description
                        )
                    }
                    _uiState.update {
                        it.copy(services = categoryList )
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message)
                    }
                }
            }
        }
    }
    fun loadServicesWorker() {
        viewModelScope.launch {
            val request = ServiceListRequest(
                limit  = "60",
                offset = "0"
            )
            when (val result = providerBookingUseCases.getService(request)) {
                is JtApiResult.Success -> {
                        _uiState.update { current ->
                            val newServiceList = result.data.data.map { serviceData ->
                                Services(
                                    title = serviceData.service.title,
                                    description = serviceData.description,
                                    image = serviceData.service.image,
                                    id = serviceData.service.id,
                                    catId = serviceData.category.id,
                                    iD = serviceData.id
                                )
                            }
                            current.copy(
                                searchServices = newServiceList
                            )
                        }
                }

                is JtApiResult.Failure -> _uiState.update {
                    it.copy(
                        message = "Failed To Load Services"
                    )
                }
            }
        }
    }

    fun getSearchServices(
        keyword: String,
        limit: String,
        offset: String,
        isInitialLoad: Boolean
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoadingMore = true) }

            val request = SearchServiceRequest(
                searchKey = keyword,
                limit = limit,
                offset = offset
            )

            when (val result = generalUseCases.searchService(request)) {
                is JtApiResult.Success -> {
                    val newServices = result.data.data.map { service ->
                        Services(
                            title = service.title,
                            description = service.description,
                            image = service.image,
                            catId = service.category.id,
                            iD = service.id
                        )
                    }.filter { it.title?.contains(uiState.value.search?:"", ignoreCase = true) == true }

                    val limitInt = limit.toIntOrNull() ?: 0
                    val hasMoreData = newServices.size >= limitInt

                    _uiState.update { currentState ->
                        val updatedList = if (isInitialLoad) newServices else currentState.searchServices + newServices
                        val newOffset = if (isInitialLoad) newServices.size else currentState.paginationOffset + newServices.size

                        currentState.copy(
                            searchServices = updatedList,
                            paginationOffset = newOffset,
                            hasMoreData = hasMoreData,
                            isLoadingMore = false
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
                }
            }
        }
    }



}