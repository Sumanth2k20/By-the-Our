package com.bytheour.presentations.screens.tabs.bookings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bytheour.core.JtApiResult
import com.bytheour.data.request.GetWorkerSpecificServicesRequest
import com.bytheour.data.request.GetWorkersRatingsRequest
import com.bytheour.useCases.UserBookingsUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WorkerServiceItem(
    val id: String,
    val rate: Double,
    val minHour: Int,
    val radius: Float,
    val servicePremises: String,
    val title: String,
    val image: String,
    val categoryTitle: String
)

data class WorkerDetailsUi(
    val id: String,
    val firstName: String,
    val lastName: String,
    val username: String,
    val verificationStatus: String,
    val experience: Int,
    val serviceAtPremises: String,
    val about: String,
    val contactCode: String,
    val contactNumber: String,
    val profileImage: String,
    val workAreaRadius: Int,
)

// New data class for rating information
data class WorkerRatingItem(
    val id: String,
    val rating: Int,
    val review: String?,
    val date: String,
    val type: String,
    val serviceTitle: String? = null,
    val userFirstName: String,
    val userLastName: String,
    val userProfile: String,
    val takenRating: TakenRating?
)


data class TakenRating(
    val id: String,
    val rating: Int,
    val serviceRating: Int?,
    val serviceRatingText: String?,
    val review: String?,
    val type: String,
    val date: String,
    val allowToModify: Boolean
)

class ViewProviderProfileViewModel(
    private val userBookingsUseCases: UserBookingsUseCases
) : ViewModel() {

    companion object {
        private const val PAGINATION_LIMIT = 10 // Add this if not defined elsewhere
    }

    data class ServiceUiState(
        val serviceDetails: List<WorkerServiceItem> = emptyList(),
        val workerDetails: WorkerDetailsUi? = null,
        val providerImages: List<String> = emptyList(),
        val providerDescription: String = "",
        val message: String? = null,
        val serviceId: String? = null,
        val title: String? = null,
        // Added rating fields
        val workerRatings: List<WorkerRatingItem> = emptyList(),
        val ratingsMessage: String? = null,
        val isLoadingRatings: Boolean = false,
        val hasMoreRatings: Boolean = true,
        val currentRatingPage: Int = 0,
        val totalRatingsCount: Int = 0
    )

    private val _uiStates = MutableStateFlow(ServiceUiState())
    val uiStates = _uiStates.asStateFlow()

    private var currentWorkerId: String? = null

    fun getWorkersSpecificServices(
        workerId: String,
    ){
        viewModelScope.launch {
            val request = GetWorkerSpecificServicesRequest(
                workerId = workerId
            )
            when(val result = userBookingsUseCases.getWorkerSpecificServices(request)){
                is JtApiResult.Success -> {
                    val response = result.data
                    val services = response.data.map { item ->
                        WorkerServiceItem(
                            id = item.id,
                            rate = item.rate,
                            minHour = item.minHour,
                            radius = item.radius?:0f,
                            servicePremises = item.serviceAtPremises,
                            title = item.service.title,
                            image = item.service.image,
                            categoryTitle = item.service.categoryTitle
                        )
                    }
                    _uiStates.update {
                        it.copy(
                            serviceDetails = services,
                            message = response.meta.message
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            message = result.error.message
                        )
                    }
                }
            }
        }
    }

//    fun getServiceProvidersDetails(
//        serviceId: String,
//        userName: String,
//    ) {
//        viewModelScope.launch {
//            val request = GetServiceProviderRequest(
//                service = serviceId,
//                providerName = userName,
//            )
//
//            when (val result = userBookingsUseCases.getProviderDetailsAndServices(request)) {
//                is JtApiResult.Success -> {
//                    val response = result.data
//
//                    val workerServiceItems = response.data.flatMap { data ->
//                        data.services.map { service ->
//                            WorkerServiceItem(
//                                id = service.id,
//                                rate = service.rate.toDouble(),
//                                minHour = service.minHour,
//                                radius = service.radius.toFloat(),
//                                servicePremises = service.serviceAtPremises,
//                                title = service.service.title,
//                                image = service.service.image,
//                                categoryTitle = service.category.title
//                            )
//                        }
//                    }
//
//                    val workerDetailsUi = response.data.firstOrNull()?.worker?.let { worker ->
//                        WorkerDetailsUi(
//                            id = worker.id,
//                            firstName = worker.firstName,
//                            lastName = worker.lastName,
//                            username = worker.username,
//                            verificationStatus = worker.verificationStatus,
//                            experience = worker.experience,
//                            serviceAtPremises = worker.serviceAtPremises,
//                            about = worker.about,
//                            contactCode = worker.contact.code,
//                            contactNumber = worker.contact.number,
//                            profileImage = worker.profile,
//                            workAreaRadius = worker.workArea.radius,
//                        )
//                    }
//                    _uiStates.update {
//                        it.copy(
//                            serviceDetails = workerServiceItems,
//                            workerDetails = workerDetailsUi
//                        )
//                    }
//                }
//
//                is JtApiResult.Failure -> {
//                    _uiStates.update {
//                        it.copy(
//                            message = result.error.message
//                        )
//                    }
//                }
//            }
//        }
//    }

    fun getWorkersRating(
        ratings: String = "0",
        workerId: String
    ) {
        if (currentWorkerId != workerId) {
            currentWorkerId = workerId
        }
        getWorkersRatingData(ratings, workerId, isRefresh = true)
    }

    private fun getWorkersRatingData(
        ratings: String,
        workerId: String,
        isRefresh: Boolean = false
    ) {
        val currentState = _uiStates.value
        if (currentState.isLoadingRatings) return

        if (!currentState.hasMoreRatings && !isRefresh) return

        viewModelScope.launch {
            val page = if (isRefresh) 0 else currentState.currentRatingPage
            val offset = (page * PAGINATION_LIMIT).toString()

            _uiStates.update { it.copy(isLoadingRatings = true) }

            val request = GetWorkersRatingsRequest(
                limit = PAGINATION_LIMIT.toString(),
                offset = offset,
                ratings = ratings,
                worker = workerId
            )

            when (val result = userBookingsUseCases.getWorkerRatings(request)) {
                is JtApiResult.Success -> {
                    val response = result.data
                    val newRatingItems = response.data.map { ratingData ->
                        WorkerRatingItem(
                            id = ratingData.id,
                            rating = ratingData.taken?.rating ?: 0,
                            review = ratingData.taken?.review ?: "",
                            date = ratingData.taken?.date ?: "",
                            type = ratingData.taken?.type ?: "",
                            serviceTitle = ratingData.service.title,
                            userFirstName = ratingData.user.firstName,
                            userLastName = ratingData.user.lastName,
                            userProfile = ratingData.user.profile,
                            takenRating = ratingData.taken?.let { taken ->
                                TakenRating(
                                    id = taken.id,
                                    rating = taken.rating,
                                    serviceRating = taken.serviceRating,
                                    serviceRatingText = taken.serviceRatingText,
                                    review = taken.review,
                                    type = taken.type,
                                    date = taken.date,
                                    allowToModify = taken.allowToModity
                                )
                            }
                        )
                    }

                    val currentItems = if (isRefresh) emptyList() else currentState.workerRatings
                    val updatedItems = currentItems + newRatingItems

                    // Since API doesn't provide total count, determine hasMore based on returned items
                    // If we get fewer items than the limit, we've reached the end
                    val hasMore = newRatingItems.size >= PAGINATION_LIMIT

                    // Calculate total count as current items count (this will grow with each page)
                    val totalCount = updatedItems.size

                    _uiStates.update {
                        it.copy(
                            workerRatings = updatedItems,
                            currentRatingPage = page + 1,
                            hasMoreRatings = hasMore,
                            isLoadingRatings = false,
                            totalRatingsCount = totalCount,
                            ratingsMessage = null
                        )
                    }
                }

                is JtApiResult.Failure -> {
                    _uiStates.update {
                        it.copy(
                            ratingsMessage = result.error.message,
                            isLoadingRatings = false
                        )
                    }
                }
            }
        }
    }
}