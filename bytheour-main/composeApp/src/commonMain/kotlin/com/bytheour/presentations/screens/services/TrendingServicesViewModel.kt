package com.bytheour.presentations.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.GetServicesRequest
import com.bytheour.data.request.SearchServiceRequest
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.UserUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import com.bytheour.utils.PAGINATION_LIMIT_15
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TrendingServicesViewModel(
    private val userUseCase: UserUseCases,
    private val generalUseCases: GeneralUseCases
) : ViewModel() {

    data class Services(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = "",
        val inspectionRate:Double? = null,
    )

    data class ServicesUiState(
        val services: List<Services> = emptyList(),
        val searchServices: List<Services> = emptyList(),
        val message: String? = null,
        val search: String? = "",
        val paginationLimit: Int = 0,
        val isSearchMode: Boolean = false,
    )

    private val _uiState = MutableStateFlow(ServicesUiState())
    val uiState: StateFlow<ServicesUiState> = _uiState.asStateFlow()

    private var currentCategoryId: String = ""

    private var searchJob: Job? = null

    fun updateSearchAndSearch(keyword: String) {
        _uiState.update { it.copy(search = keyword) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            val refinedKeyword = keyword.trim().split(" ").joinToString("%") //  space replacement like lawn%s
            getSearchServices(
                keyword = refinedKeyword,
                limit = "10",
                offset = "0"
            )
        }
    }

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
                services = emptyList(),
                paginationLimit = 0,
                isSearchMode = false
            )
        }
        loadInitialServices(currentCategoryId, searchQuery)
    }

    fun loadMoreSearchItem() {
        val currentState = _uiState.value
        val newOffset = currentState.paginationLimit + PAGINATION_LIMIT
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(300)
            val refinedKeyword = _uiState.value.search?.trim()?.split(" ")?.joinToString("%") ?: "" //  space replacement like lawn%s
            getSearchServices(
                keyword = refinedKeyword,
                limit = "10",
                offset = newOffset.toString()
            )
        }
    }

    fun loadInitialServices(catId: String, keyword: String = "") {
        currentCategoryId = catId
        _uiState.update {
            it.copy(
                services = emptyList(),
                paginationLimit = 0
            )
        }
        loadServices(catId, "0", true, keyword = keyword)
    }

    fun loadMoreServices() {
        val currentState = _uiState.value
        val newOffset = currentState.paginationLimit + PAGINATION_LIMIT
        loadServices(currentCategoryId, newOffset.toString(), false,keyword = "")
    }

    private fun loadServices(catId: String, offset: String, isInitialLoad: Boolean, keyword: String) {
        viewModelScope.launch {
            val request = GetServicesRequest(
                categoryId = catId,
                limit = PAGINATION_LIMIT_15.toString(),
                offset = offset,
                keyword = keyword
            )

            val result = generalUseCases.getServices(request)
            when (result) {
                is JtApiResult.Success -> {
                    val newServiceList = result.data?.data?.map { serviceData ->
                        Services(
                            title = serviceData.title,
                            description = serviceData.description,
                            image = serviceData.image,
                            id = serviceData.id,
                            inspectionRate = serviceData.inspectionRate
                        )
                    } ?: emptyList()

                    _uiState.update { currentState ->
                        val updatedServices = if (isInitialLoad) {
                            newServiceList
                        } else {
                            currentState.services + newServiceList
                        }

                        currentState.copy(
                            services = updatedServices,
                            paginationLimit = offset.toInt(),
                            message = null
                        )
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


    fun getTrendingServices() {
        viewModelScope.launch {
            val result = userUseCase.getTrendingServices()
            when (result) {
                is JtApiResult.Success -> {
                    val serviceList = result.data.data.map { serviceList ->
                        Services(
                            title = serviceList.title,
                            description = serviceList.description,
                            image = serviceList.image,
                            id = serviceList.id,
                            inspectionRate = serviceList.inspectionRate
                        )
                    }
                    _uiState.update {
                        it.copy(services = serviceList)
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

    fun getSearchServices(
        keyword: String,
        limit: String,
        offset: String
    ) {
        viewModelScope.launch {
            val trimmedKeyword = keyword
            val request = SearchServiceRequest(
                searchKey = trimmedKeyword,
                limit = limit,
                offset = offset
            )
            when (val result = generalUseCases.searchService(request)) {
                is JtApiResult.Success -> {
                    val serviceList = result.data.data.map { service ->
                        Services(
                            title = service.title,
                            description = service.description,
                            image = service.image,
                            id = service.id,
                            inspectionRate = service.inspectionRate
                        )
                    }



                    _uiState.update {currentState->
                        val updatedServices = if ((offset.toIntOrNull() ?: 0) <= 0) {
                            serviceList
                        } else {
                            currentState.searchServices + serviceList
                        }

                        currentState.copy(
                            searchServices = updatedServices,
                            paginationLimit = offset.toIntOrNull() ?: 0,
                            isSearchMode = true
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(message = result.error.message,
                            )
                    }
                }
            }
        }
    }

}
