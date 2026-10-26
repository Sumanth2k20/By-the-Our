package com.bytheour.presentations.screens.tabs.servicesTab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.GetServicesRequest
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.utils.PAGINATION_LIMIT_15
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ProviderSelectServiceScreenViewModel(
    private val generalUseCases: GeneralUseCases,
) : ViewModel() {

    data class Services(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = "",
        val catId:String? = "",
        val iD:String? = null
    )

    data class ServicesUiState(
        val services: List<Services> = emptyList(),
        val searchServices: List<Services> = emptyList(),
        val message: String? = null,
        val search: String? = "",
        val paginationLimit: Int = 0,
        val searchPaginationLimit: Int = 0,
        val catName:String = "",
        val isSearchMode: Boolean = false,
        val hasMoreSearchResults: Boolean = true,
        val count :Int? = 0
    )

    private val _uiState = MutableStateFlow(ServicesUiState())
    val uiState: StateFlow<ServicesUiState> = _uiState.asStateFlow()

    private var currentCategoryId: String = ""
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
                services = emptyList(),
                paginationLimit = 0,
                isSearchMode = false
            )
        }
        loadInitialServices(currentCategoryId, searchQuery)
    }


    fun loadInitialServices(catId: String, keyword: String = "") {
        currentCategoryId = catId
        _uiState.update {
            it.copy(
                services = emptyList(),
                paginationLimit = 0,
                isSearchMode = false
            )
        }
        loadServices(catId, "0", true, keyword = keyword)
    }

    fun loadMoreServices() {
        val currentState = _uiState.value
        val newOffset = currentState.paginationLimit + PAGINATION_LIMIT_15
        loadServices(currentCategoryId, newOffset.toString(), false, keyword = "")
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
                            catId = serviceData.category.id,
                            iD = serviceData.id
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
                            message = null,
                            catName = (result.data?.data?.map { it.category.title }?.firstOrNull() ?: ""),
                            count = result.data?.meta?.count
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
}