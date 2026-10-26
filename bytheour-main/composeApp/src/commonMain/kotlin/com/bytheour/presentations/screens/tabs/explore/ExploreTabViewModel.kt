package com.bytheour.presentations.screens.tabs.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.GetServicesRequest
import com.bytheour.useCases.GeneralUseCases
import com.bytheour.useCases.ProviderServicesUseCases
import com.bytheour.useCases.UserUseCases
import com.bytheour.utils.PAGINATION_LIMIT
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ExploreTabViewModel(
    private val generalUseCases: GeneralUseCases,
    private val userUseCase: UserUseCases,
    private val providerServicesUseCases: ProviderServicesUseCases,
):ViewModel() {

    data class Categories(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = ""
    )
    data class Services(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = ""
    )
    data class EquipmentRentals(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = ""
    )
    data class FacilityRentals(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = ""
    )
    data class TrendingServices(
        val title: String? = "",
        val description: String? = "",
        val image: String? = "",
        val id: String? = ""
    )

    data class ExploreUiSate(
        val latitude: Double = 0.0,
        val longitude: Double = 0.0,
        val flatNo: String? = null,
        val address: String? = null,
        val landmark: String? = null,
        val pinCode: String? = null,
        val country: String? = null,
        val hasPermissionDenied:Boolean = false,
        val deviceLocationAccess:Boolean = false,
        val isLocationFetch : Boolean = true,
        val services: List<Services> = emptyList(),
        val categories: List<Categories> = emptyList(),
        val equipmentRentals: List<EquipmentRentals> = emptyList(),
        val facilityRentals: List<FacilityRentals> = emptyList(),
        val trendingServices: List<TrendingServices> = emptyList(),
        val message: String? = null,
        // Pagination states
        val hasMoreEquipmentData: Boolean = true,
        val hasMoreFacilityData: Boolean = true,
        val isLoadingMoreEquipment: Boolean = false,
        val isLoadingMoreFacility: Boolean = false,
        val currentEquipmentPage: Int = 0,
        val currentFacilityPage: Int = 0,
        val equipmentCategoryId: String? = null,
        val facilityCategoryId: String? = null,
        val notificationPermissionRequested: Boolean = false

    )

    private val _uiState = MutableStateFlow(ExploreUiSate())
    val uiState: StateFlow<ExploreUiSate> = _uiState.asStateFlow()

    fun updateFullAddress(flatNo: String?, address: String?, landmark: String?, pinCode: String?,country:String?) {
        _uiState.update { state ->
            state.copy(
                flatNo = flatNo,
                address = address?.let { if (pinCode != null) "$it, $pinCode" else it },
                landmark = landmark,
                country = country
            )
        }
    }
    fun setNotificationPermissionRequested() {
        _uiState.value = _uiState.value.copy(notificationPermissionRequested = true)
    }
    fun getCategories() {
        viewModelScope.launch {
            when (val result = providerServicesUseCases.getCategory()) {
                is JtApiResult.Success -> {
                    val categoryList = result.data.data.map { serviceList ->
                        Categories(
                            title = serviceList.title,
                            description = serviceList.description,
                            image = serviceList.image,
                            id = serviceList.id,
                        )
                    }
                    _uiState.update {
                        it.copy(categories = categoryList )
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

    fun fetchEquipmentRentals(
        categoryId: String,
        limit: String,
        offset: String
    ) {
        fetchEquipmentRentalsInternal(
            categoryId = categoryId,
            limit = limit,
            offset = offset,
            isPaginated = false,
            isRefresh = false
        )
    }

    fun fetchFacilityRentals(
        categoryId: String,
        limit: String,
        offset: String
    ) {
        fetchFacilityRentalsInternal(
            categoryId = categoryId,
            limit = limit,
            offset = offset,
            isPaginated = false,
            isRefresh = false
        )
    }

    fun loadEquipmentRentals(
        categoryId: String,
        limit: Int = PAGINATION_LIMIT
    ) {
        _uiState.update {
            it.copy(equipmentCategoryId = categoryId)
        }
        fetchEquipmentRentalsInternal(
            categoryId = categoryId,
            limit = limit.toString(),
            offset = "0",
            isPaginated = true,
            isRefresh = true
        )
    }

    fun loadFacilityRentals(
        categoryId: String,
        limit: Int = PAGINATION_LIMIT
    ) {
        _uiState.update {
            it.copy(facilityCategoryId = categoryId)
        }
        fetchFacilityRentalsInternal(
            categoryId = categoryId,
            limit = limit.toString(),
            offset = "0",
            isPaginated = true,
            isRefresh = true
        )
    }

    fun loadMoreEquipmentRentals(limit: Int = PAGINATION_LIMIT) {
        val currentState = _uiState.value
        currentState.equipmentCategoryId?.let { categoryId ->
            fetchEquipmentRentalsInternal(
                categoryId = categoryId,
                limit = limit.toString(),
                offset = "0", // Will be recalculated in the internal method
                isPaginated = true,
                isRefresh = false
            )
        }
    }

    fun loadMoreFacilityRentals(limit: Int = PAGINATION_LIMIT) {
        val currentState = _uiState.value
        currentState.facilityCategoryId?.let { categoryId ->
            fetchFacilityRentalsInternal(
                categoryId = categoryId,
                limit = limit.toString(),
                offset = "0", // Will be recalculated in the internal method
                isPaginated = true,
                isRefresh = false
            )
        }
    }

    private fun fetchEquipmentRentalsInternal(
        categoryId: String,
        limit: String,
        offset: String,
        isPaginated: Boolean,
        isRefresh: Boolean
    ) {
        val currentState = _uiState.value

        if (isPaginated && currentState.isLoadingMoreEquipment) return
        if (isPaginated && !currentState.hasMoreEquipmentData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentEquipmentPage
            val calculatedOffset = if (isPaginated) (page * limit.toInt()).toString() else offset

            if (isPaginated) {
                _uiState.update {
                    it.copy(isLoadingMoreEquipment = true)
                }
            }

            val request = GetServicesRequest(
                categoryId = categoryId,
                limit = limit,
                offset = calculatedOffset
            )
            when (val result = generalUseCases.getServices(request)) {
                is JtApiResult.Success -> {
                    val newServiceList = result.data?.data?.map { serviceList ->
                        EquipmentRentals(
                            title = serviceList.title,
                            description = serviceList.description,
                            image = serviceList.image,
                            id = serviceList.id
                        )
                    } ?: emptyList()

                    if (isPaginated) {
                        val currentItems = if (isRefresh) emptyList() else currentState.equipmentRentals
                        val updatedItems = currentItems + newServiceList
                        // If we get fewer items than requested, assume no more data
                        val hasMore = newServiceList.size >= limit.toInt()

                        _uiState.update {
                            it.copy(
                                equipmentRentals = updatedItems,
                                currentEquipmentPage = page + 1,
                                hasMoreEquipmentData = hasMore,
                                isLoadingMoreEquipment = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(equipmentRentals = newServiceList)
                        }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMoreEquipment = if (isPaginated) false else it.isLoadingMoreEquipment
                        )
                    }
                }
            }
        }
    }

    private fun fetchFacilityRentalsInternal(
        categoryId: String,
        limit: String,
        offset: String,
        isPaginated: Boolean,
        isRefresh: Boolean
    ) {
        val currentState = _uiState.value

        if (isPaginated && currentState.isLoadingMoreFacility) return
        if (isPaginated && !currentState.hasMoreFacilityData && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentFacilityPage
            val calculatedOffset = if (isPaginated) (page * limit.toInt()).toString() else offset

            if (isPaginated) {
                _uiState.update {
                    it.copy(isLoadingMoreFacility = true)
                }
            }

            val request = GetServicesRequest(
                categoryId = categoryId,
                limit = limit,
                offset = calculatedOffset
            )
            when (val result = generalUseCases.getServices(request)) {
                is JtApiResult.Success -> {
                    val newServiceList = result.data?.data?.map { serviceList ->
                        FacilityRentals(
                            title = serviceList.title,
                            description = serviceList.description,
                            image = serviceList.image,
                            id = serviceList.id
                        )
                    } ?: emptyList()

                    if (isPaginated) {
                        val currentItems = if (isRefresh) emptyList() else currentState.facilityRentals
                        val updatedItems = currentItems + newServiceList
                        // If we get fewer items than requested, assume no more data
                        val hasMore = newServiceList.size >= limit.toInt()

                        _uiState.update {
                            it.copy(
                                facilityRentals = updatedItems,
                                currentFacilityPage = page + 1,
                                hasMoreFacilityData = hasMore,
                                isLoadingMoreFacility = false
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(facilityRentals = newServiceList)
                        }
                    }
                }
                is JtApiResult.Failure -> {
                    _uiState.update {
                        it.copy(
                            message = result.error.message,
                            isLoadingMoreFacility = if (isPaginated) false else it.isLoadingMoreFacility
                        )
                    }
                }
            }
        }
    }

//    fun getExpertVideos(){
//        viewModelScope.launch {
//            when (val result = generalUseCases.getExportsVideos()) {
//                is JtApiResult.Success -> {
//                    _uiState.update {
//                        it.copy(
//                            message = result.data.meta.message
//                        )
//                    }
//                }
//                is JtApiResult.Failure -> {
//                    _uiState.update {
//                        it.copy(message = result.error.message)
//                    }
//                }
//            }
//        }
//    }

    fun getTrendingServices() {
        viewModelScope.launch {
            when (val result = userUseCase.getTrendingServices()) {
                is JtApiResult.Success -> {
                    val serviceList = result.data.data.map { serviceList ->
                        TrendingServices(
                            title = serviceList.title,
                            description = serviceList.description,
                            image = serviceList.image,
                            id = serviceList.id
                        )
                    }
                    _uiState.update {
                        it.copy(trendingServices = serviceList)
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

